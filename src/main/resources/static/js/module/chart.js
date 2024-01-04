const LevelData = async function() {
    try{
        const response = fetch('/getLevelChart',{
            method:'POST'
        });
        const data = await response.json();
    } catch (error){
        console.log("에러");
    }
    return data;
}

const WinRateData = async function() {
    try{
        const response = fetch('/getWinRateChart',{
            method:'POST'
        });
        const data = await response.json();
    } catch (error){
        console.log("에러");
    }
    return data;
}

const PointData = async function() {
    try{
        const response = fetch('/getPointChart',{
            method:'POST'
        });
        const data = await response.json();
    } catch (error){
        console.log("에러");
    }
    return data;
}