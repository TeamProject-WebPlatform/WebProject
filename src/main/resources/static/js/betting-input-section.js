function insertBettingInputsection(state,nickname,hostNick,clientNick,result,btId,postId,point){
    let html = ``;
    if(state!='T'){
        html += `
    <div class="search">
        <div class="search__input">
            <i class="fa-solid fa-coins ico_fontawesome"></i>`;
            if(state=='N'){
                html += `
                <battleStateCheck>
                    <input class="point-betting-input" type="search" name="search" placeholder="MATCHING" readonly>
                </battleStateCheck>`
            }else if(state=='A'){
                html += `
                <battleStateCheck>`;
                if(nickname){
                    html +=`
                    <logincheck>
                        <input class="point-betting-input" type="search" name="search" placeholder="BETTING HERE">
                    </logincheck>`
                }else{
                    html +=`
                    <input class="point-betting-input"type="search" name="search" placeholder="LOGIN FIRST" readonly>`;
                }
                html +=
                `</battleStateCheck>`;
            }else if(state=='P'){
                html+=`
                <battleStateCheck>
                    <input class="point-betting-input" type="search" name="search" placeholder="PROCEEDING" readonly>
                </battleStateCheck>`
            }else if(state=='B'){
                html+=`
                <battleStateCheck>
                    <input class="point-betting-input" type="search" name="search" placeholder="AWAITING" readonly>
                </battleStateCheck>`
            }else if(state=='H' || state=='C' || state=='E'){
                html+=`
                <battleStateCheck>
                    <input class="point-betting-input" type="search" name="search" placeholder="TERMINATED" readonly>
                </battleStateCheck>`
            }
        html +=`
        </div>
    </div>`
    }else if(state=='T'){
        let formattedPoint = (2*Number(point)).toLocaleString();
        let inactive="";
        html += `<battleStateCheck>`;
        if(hostNick==nickname){
            if(result=='1'){
                inactive = "inactive";
            }
            html +=`
            <div class="receive_btn ${inactive}" dead="false" onclick="receivePoint(${btId},${postId})">
                <div class="click">CLICK!</div>
                <i class="fa-solid fa-coins ico_fontawesome"></i>
                <div>
                    <div>${formattedPoint}</div>
                    <div class="pointFloating">${formattedPoint}</div>
                </div>
            </div>`
        }else if(clientNick==nickname){
            console.log(clientNick);
            console.log(nickname);
            console.log(result);
            if(result=='0'){
                console.log("꿋");
                inactive = "inactive";
            }
            html +=`
            <div class="receive_btn ${inactive}" dead="false" onclick="receivePoint(${btId},${postId})">
                <div class="click">CLICK!</div>
                <i class="fa-solid fa-coins ico_fontawesome"></i>
                <div>
                    <div>${formattedPoint}</div>
                    <div class="pointFloating">${formattedPoint}</div>
                </div>
            </div>`
        }
        html += `</battleStateCheck>`;
    }
    return html;
}
