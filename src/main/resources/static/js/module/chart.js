const LevelData = async function() {
    var LevelData = [];
    try{
        const response = await fetch('/getLevelChart',{
            method:'POST'
        });
        const data = await response.json();
        for(let i=0;i<data.length;i++){
            LevelData.push(data[i]);
        }
    } catch (error){
        console.log(error);
    }
    return LevelData;
}

const LevelRanking = async function(){
    var LvlRankTable = [];
    try{
        const response = await fetch('/getLevelTable',{
            method:'POST'
        });
        const data = await response.json();
        for(let i=0;i<data.length;i++){
            LvlRankTable.push(data[i]);
        }
    } catch (error){
        console.log(error);
    }
    return LvlRankTable;
}

const WinRateRanking = async function(){
    var WrRankTable = [];
    try{
        const response = await fetch('/getWinRateTable',{
            method:'POST'
        });
        const data = await response.json();
        for(let i=0;i<data.length;i++){
            WrRankTable.push(data[i]);
        }
    } catch (error){
        console.log(error);
    }
    return WrRankTable;
}

const PointRanking = async function(){
    var PointRankTable = [];
    try{
        const response = await fetch('/getPointTable',{
            method:'POST'
        });
        const data = await response.json();
        for(let i=0;i<data.length;i++){
            PointRankTable.push(data[i]);
        }
    } catch (error){
        console.log(error);
    }
    return PointRankTable;
}

const WinRateData = async function() {
    var WinRateData = [];
    try{
        const response = await fetch('/getWinRateChart',{
            method:'POST'
        });
        const data = await response.json();
        for(let i=0;i<data.length;i++){
            WinRateData.push(data[i]);
        }
    } catch (error){
        console.log("에러");
    }
    return WinRateData;
}

const PointData = async function() {
    var PointData = [];
    try{
        const response = await fetch('/getPointChart',{
            method:'POST'
        });
        const data = await response.json();
        for(let i=0;i<data.length;i++){
            PointData.push(data[i]);
        }
    } catch (error){
        console.log("에러");
    }
    return PointData;
}

const LevelChart = async function() {
    let Level = await LevelData();
    var WinChart = new Chart(
        document.getElementById('levels'),
        {
            type: 'line',
            data: {
                labels: [],
                datasets: [
                    {
                    label: '레벨 구간 별 유저수',
                    data: ({0:Level[0], 10:Level[1], 20:Level[2], 30:Level[3], 40:Level[4], 50:Level[5], 60:Level[6], 70:Level[7], 80:Level[8], 90:Level[9]}),
                    borderColor: 'rgb(75, 0, 192)',
                    fill: false,
                    stepped: false,
                    backgroundColor: 'skyblue',
                    type:'bar'
                    }
                ]
                },
            options: {
                responsive: false,
                interaction: {
                intersect: false,
                axis: 'x'
                },
                plugins: {
                title: {
                    display: true,
                    text: (ctx) => '레벨 구간 별 유저 분포도',
                },
                legend:{
                    display : false
                }
                }
            }
        });
}

const WinRateChart = async function() {
    let WinRate = await WinRateData();
    var WinChart = new Chart(
        document.getElementById('win'),
        {
            type: 'line',
            data: {
                labels: [],
                datasets: [
                    {
                    label: '승률 별 유저수',
                    data: ({0:WinRate[0], 10:WinRate[1], 20:WinRate[2], 30:WinRate[3], 40:WinRate[4], 50:WinRate[5], 60:WinRate[6], 70:WinRate[7], 80:WinRate[8], 90:WinRate[9]}),
                    borderColor: 'rgb(75, 0, 192)',
                    fill: false,
                    stepped: false,
                    backgroundColor: 'skyblue',
                    type:'bar'
                    }
                ]
                },
            options: {
                responsive: false,
                interaction: {
                intersect: false,
                axis: 'x'
                },
                plugins: {
                title: {
                    display: true,
                    text: (ctx) => '승률 별 유저 분포도',
                },
                legend:{
                    display : false
                }
                }
            }
        });
}

const PointChart = async function() {
    let Point = await PointData();
    var PointChart = new Chart(
        document.getElementById('point'),
        {
            type: 'line',
            data: {
                labels: [],
                datasets: [
                    {
                    label: '포인트 보유 별 유저수',
                    data: ({0:Point[0], 400:Point[1], 800:Point[2], 1200:Point[3], 1600:Point[4]}),
                    borderColor: 'rgb(75, 0, 192)',
                    fill: false,
                    stepped: false,
                    backgroundColor: 'skyblue',
                    type:'bar'
                    }
                ]
                },
            options: {
                responsive: false,
                interaction: {
                intersect: false,
                axis: 'x'
                },
                plugins: {
                title: {
                    display: true,
                    text: (ctx) => '포인트 보유량 유저 분포도',
                },
                legend:{
                    display : false
                }
                }
            }
        });
}