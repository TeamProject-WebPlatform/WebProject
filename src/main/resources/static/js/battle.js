let battleCardStompClient = null;

document.addEventListener("DOMContentLoaded", function () {
    let logo_img_paths = [getContextPath()+"/img/logo_white.png", getContextPath()+"/img/logo_black.png"];
    const vs = document.querySelectorAll('.vs img');
    let isDark = getCookie('darkMode');
    vs.forEach(function(x){
            if (isDark === 'Y') {
                x.src = logo_img_paths[0];
            }
            else {
                x.src = logo_img_paths[1];
            }
        })
    document.getElementById("toggle").addEventListener("click", function () {
        isDark = getCookie('darkMode');
        vs.forEach(function(x){
            if (isDark === 'Y') {
                x.src = logo_img_paths[0];
            }
            else {
                x.src = logo_img_paths[1];
            }
        })
    });
    const active = document.getElementById("nav-battle");
    active.classList.add("uk-active");
    
    const pointBarLefts = document.querySelectorAll(".info-center-left .info-point-bar");
    const pointBarRights = document.querySelectorAll(".info-center-right .info-point-bar");

    const pointLefts = document.querySelectorAll(".info-center-left .info-point-percent");
    const pointRights = document.querySelectorAll(".info-center-right .info-point-percent");

    for (let i = 0; i < pointBarLefts.length; i++) {
        let pointBarLeft = pointBarLefts.item(i);
        pointBarLeft.style.width = pointLefts[i].innerHTML;
        pointBarLeft.style.backgroundColor = getColorRatio(Number(pointLefts[i].innerHTML.replace("%", "")));
        pointLefts[i].style.color = getColorRatio(Number(pointLefts[i].innerHTML.replace("%", "")));

        let pointBarRight = pointBarRights.item(i);
        pointBarRight.style.width = pointRights[i].innerHTML;
        pointBarRight.style.backgroundColor = getColorRatio(Number(pointRights[i].innerHTML.replace("%", "")));
        pointRights[i].style.color = getColorRatio(Number(pointRights[i].innerHTML.replace("%", "")));
    }
    battleCardConnect();
});
function battleCardConnect() {
    var socket = new SockJS('/ws');
    battleCardStompClient = Stomp.over(socket);
    // 보유 포인트 구독

    // 배틀 전광판 구독
    const cards = document.querySelectorAll(".battle-card-info");
    battleCardStompClient.connect({}, function (frame) {
        cards.forEach(function (card) {
            let btId = card.getAttribute("btId");
            battleCardStompClient.subscribe(`/topic/pointbetting/` + btId, function (response) {
                try {
                    let pointTO = JSON.parse(response.body);
                    let pointInfo = document.querySelector(".battle-card-info[btId='" + btId + "']");
                    if (pointTO.flag === -1) {
                        return;
                    }
                    pointInfo.querySelector(".host-total-point").innerHTML = formatNumberTok(pointTO.hostTotalPoint);
                    pointInfo.querySelector(".host-total-mem-no").innerHTML = pointTO.hostTotalMemNo;
                    pointInfo.querySelector(".host-point-reward-ratio").innerHTML = "X " + pointTO.hostPointRewardRatio;
                    let hostPointRatio = pointInfo.querySelector(".host-point-ratio");
                    let pointBarLeft = pointInfo.querySelector(".info-center-left .info-point-bar");
                    let pointLeft = pointInfo.querySelector(".info-center-left .info-point-percent");
                    hostPointRatio.innerHTML = pointTO.hostPointRatio + "%";
                    hostPointRatio.style.color = getColorRatio(Number(pointLeft.innerHTML.replace("%", "")));
                    pointBarLeft.style.width = pointLeft.innerHTML;
                    pointBarLeft.style.backgroundColor = getColorRatio(Number(pointLeft.innerHTML.replace("%", "")));

                    pointInfo.querySelector(".client-total-point").innerHTML = formatNumberTok(pointTO.clientTotalPoint);
                    pointInfo.querySelector(".client-total-mem-no").innerHTML = pointTO.clientTotalMemNo;
                    pointInfo.querySelector(".client-point-reward-ratio").innerHTML = pointTO.clientPointRewardRatio + " X";
                    let clientPointRatio = pointInfo.querySelector(".client-point-ratio");
                    let pointBarRight = pointInfo.querySelector(".info-center-right .info-point-bar");
                    let pointRight = pointInfo.querySelector(".info-center-right .info-point-percent");
                    clientPointRatio.innerHTML = pointTO.clientPointRatio + "%";
                    clientPointRatio.style.color = getColorRatio(Number(pointRight.innerHTML.replace("%", "")));
                    pointBarRight.style.width = pointRight.innerHTML;
                    pointBarRight.style.backgroundColor = getColorRatio(Number(pointRight.innerHTML.replace("%", "")));


                    let pointInputs = pointInfo.querySelector(".point-betting-input");
                    pointInputs.placeholder = "YOU ALREADY BET";
                    pointInputs.readOnly = true;
                    let buttons = pointInfo.querySelectorAll(".point-betting-button");

                    let button = null;

                    if (pointTO.flag == 0) {
                        button = buttons[0];
                        button.style.backgroundColor = "blueviolet"
                        button.style.boxShadow = "rgba(138,43,226,0.5) 0px 0px 17px 3px";
                    } else if (pointTO.flag == 1) {
                        button = buttons[1];
                        button.style.backgroundColor = "blueviolet"
                        button.style.boxShadow = "rgba(138,43,226,0.5) 0px 0px 17px 3px";
                    }

                } catch (err) {
                    console.log("에러 : " + err);
                }
            });
        });
    });
}

function betPoint(btId, flag) {
    // 포인트 배팅
    // flag : 0-왼 , 1-오
    const section = document.getElementById("bettingId" + btId);
    let pointInputs = section.querySelector(".point-betting-input");
    let point = pointInputs.value;
    if (point === "" | isNaN(point)) {
        return;
    }
    if (point > currentPoint) {
        alert("포인트 부족");
        return;
    }
    let buttons = section.querySelectorAll(".point-betting-button");
    let button;
    if (flag === 0) {
        button = buttons[0];
    } else {
        button = buttons[1];
    }
    if (!button) return;
    if (button.getAttribute("active") === "F") {
        button.setAttribute("active", "T");
        button.innerHTML = "SUBMIT";
        button.style.color = "red";
    } else if (button.getAttribute("active") === "T") {

        pointInputs.value = "";

        sendPointChange(-point, '50003');
        battleCardStompClient.send(`/app/pointbetting`, {}, JSON.stringify({
            point: point,
            btId: btId,
            flag: flag,
            memId: memId
        }));
        button.setAttribute("active", "F");
        button.innerHTML = flag === 0 ? "HOST" : "CLIENT";
        button.style.color = "white";
    }
}
function formatNumberTok(value) {
    var formattedValue = value > 999999 ? (value / 1000000).toFixed(1) + 'm' : (value > 999 ? (value / 1000).toFixed(1) + 'k' : value);
    return formattedValue;
}
function getColorRatio(bettingRatio) {
    if (bettingRatio >= 90) {
        return '#FF0000'; // 빨간색
    } else if (bettingRatio >= 70) {
        return '#FFA500'; // 주황색
    } else if (bettingRatio >= 50) {
        return '#00FF00'; // 연두색
    } else if (bettingRatio >= 30) {
        return '#00F0FF'; // 민트색
    } else {
        return '#0FCFA5'; // 검은색
    }
}
function formatNumberTok(value) {
    var formattedValue = value > 999999 ? (value / 1000000).toFixed(1) + 'm' : (value > 999 ? (value / 1000).toFixed(1) + 'k' : value);
    return formattedValue;
}