let battleCardStompClient = null;

document.addEventListener("DOMContentLoaded", function () {
    let logo_img_paths = [getContextPath() + "/img/logo_white.png", getContextPath() + "/img/logo_black.png"];
    const vs = document.querySelectorAll('.vs img');
    let isDark = getCookie('darkMode');
    vs.forEach(function (x) {
        if (isDark === 'Y') {
            x.src = logo_img_paths[0];
        }
        else {
            x.src = logo_img_paths[1];
        }
    })
    document.getElementById("toggle").addEventListener("click", function () {
        isDark = getCookie('darkMode');
        vs.forEach(function (x) {
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
    document.querySelectorAll(".left-time").forEach(function (x) {

        let delay = x.getAttribute("delay");        

        updateTimerDisplay(delay,x);
        const timerInterval = setInterval(() => {
            // 남은 시간 갱신 0.01초마다 갱신
            delay -= 10;

            // 타이머 표시 갱신
            updateTimerDisplay(delay, x);

            // 남은 시간이 0 이하일 경우 타이머 중지
            if (delay <= 0) {
                clearInterval(timerInterval);
            }
        }, 10);
    });

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
            let postId = card.getAttribute("postId");
            battleCardStompClient.subscribe(`/topic/pointbetting/` + btId, function (response) {
                try {
                    let endpoint = JSON.parse(response.body).endpoint;
                    if (endpoint === 0) {
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
                        if (pointTO.memId === memId) {
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
                        }
                    } else if (endpoint === 1) {
                        let stateTO = JSON.parse(response.body);
                        const card = document.querySelector(".battle-card__box[btId='" + btId + "']");
                        let pointInfo = document.querySelector(".battle-card-info[btId='" + btId + "']");
                        const section = pointInfo.querySelector(".info-state");
                        section.querySelectorAll("div").forEach(function (x) {
                            x.classList.add("none");
                        })
                        section.querySelector("." + stateTO.state).classList.remove("none");
                        if (stateTO.client) {
                            // 클라이언트 추가됨.
                            const clientSection = document.querySelector(".battle-card__box[btId='" + btId + "'] .battle-profile-right");
                            clientSection.querySelectorAll(".battle-profile-lvl").forEach(function (x) {
                                x.innerHTML = "Lv "+stateTO.client.level;
                            });
                            clientSection.querySelectorAll(".battle-profile-nick").forEach(function (x) {
                                x.innerHTML = stateTO.client.nickname;
                            });
                            clientSection.querySelectorAll(".battle-profile-win").forEach(function (x) {
                                x.innerHTML = stateTO.client.win +" 승";
                            });
                            clientSection.querySelectorAll(".battle-profile-lose").forEach(function (x) {
                                x.innerHTML = stateTO.client.lose+" 패";
                            });
                            clientSection.querySelectorAll(".battle-profile-slash").forEach(function (x) {
                                x.innerHTML = "&nbsp;/&nbsp;"
                            });

                            let total = stateTO.client.win + stateTO.client.lose
                            let rate = stateTO.client.win * 100.0 / total;
                            let formattedRate = rate.toFixed(2);
                            clientSection.querySelectorAll(".battle-profile-win-rate").forEach(function (x) {
                                x.innerHTML = formattedRate+"%";
                            });
                            if (stateTO.state === 'A') {
                                // 시간 작업
                                const timeDisplay = pointInfo.querySelector("left-time");
                                // 백엔드에서 받은 남은 시간(밀리초)
                                const remainingTime = stateTO.delay;
                                // 초기 남은 시간 표시
                                updateTimerDisplay(remainingTime,timeDisplay);
                                const timerInterval = setInterval(() => {
                                    // 남은 시간 갱신 0.01초마다 갱신
                                    remainingTime -= 10;

                                    // 타이머 표시 갱신
                                    updateTimerDisplay(remainingTime, timeDisplay);

                                    // 남은 시간이 0 이하일 경우 타이머 중지
                                    if (remainingTime <= 0) {
                                        clearInterval(timerInterval);
                                    }
                                }, 10);
                            }

                        }
                        console.log("상태 값 변경 : "+stateTO.state);
                        // 베팅 입력란 변경
                        // 기존 타임리프 태그 none
                        pointInfo.querySelector(".betting-input-section").style.display = "none";
                        let host = card.querySelectorAll(".battle-profile-nick")[0].innerHTML;
                        let client = card.querySelectorAll(".battle-profile-nick")[1].innerHTML;
                        // insertBettingInputsection(state,nickname,hostNick,clientNick,result,btId,postId,point)
                        pointInfo.querySelector(".betting-input-section-web").innerHTML = 
                            insertBettingInputsection(stateTO.state,nickname,host,client,stateTO.result,btId,postId,stateTO.point);
                    } else if (endpoint === 2){
                        let dsbtTO = JSON.parse(response.body);
                        const card = document.querySelector(".battle-card__box[btId='" + btId + "']");
                        let pointInfo = document.querySelector(".battle-card-info[btId='" + btId + "']");
                        let list = dsbtTO.list;
                        let point;
                        list.forEach(function(x){
                            if(x.member.memNick==nickname){
                                point = x.pointDstb;
                                return;
                            }
                        })
                        if(!point){
                            // 베팅 무관
                            pointInfo.querySelector(".betting-input-section-web").innerHTML = 
                                insertBettingInputsection('T',nickname,host,client,"0",btId,postId,point);
                        }else if(point!==0){
                            // 베팅 성공
                            pointInfo.querySelector(".betting-input-section-web").innerHTML = 
                                insertBettingInputsection('T',nickname,host,client,"-1",btId,postId,point);
                        }else if(point===0){
                            // 베팅 실패
                            pointInfo.querySelector(".betting-input-section-web").innerHTML = 
                                insertBettingInputsection('T',nickname,host,client,"-2",btId,postId,point);
                        }
                    }
                } catch (err) {
                    console.log("에러 : " + err);
                }
            });
        });
    });
}
function updateTimerDisplay(remainingTime, timeDisplay) {
    let second = Math.floor(remainingTime / 1000); //초
    if(second <= 600){
        // 남은 시간이 십분 이하일때
        timeDisplay.style.color = "#ff0000";
    }
    let hour = Math.floor(second / (60 * 60));
    let min = Math.floor(second / 60);
    min = Math.floor(min % 60);
    second = Math.floor(second % 60);
    if(hour===0){
        if(min===0){
            timeDisplay.innerHTML = second+"s";
        }else{
            timeDisplay.innerHTML = min+":"+second;
        }
    }else{
        timeDisplay.innerHTML =hour+":"+ min + ":" + second;
    }

}
function betPoint(btId, flag) {
    // 포인트 배팅
    // flag : 0-왼 , 1-오
    const section = document.querySelector(".battle-card__box[btId='" + btId + "']");
    let pointInputs = section.querySelector(".point-betting-input");
    let host = section.querySelectorAll(".battle-profile-nick")[0].innerHTML;
    let client = section.querySelectorAll(".battle-profile-nick")[1].innerHTML;
    if(nickname === host || nickname === client){
        alert('자신의 배틀에는 베팅할 수 없습니다.');
        return;
    }
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
function getGameName(code){
    let gameName = "";
    if(code=='30001') gameName = 'LeagueofLegends';
    if(code=='30002') gameName = 'BattleGround';
    if(code=='30003') gameName = 'Overwatch';
    if(code=='30004') gameName = 'Valorant';
    if(code=='30005') gameName = 'FIFA';

    return gameName;
}