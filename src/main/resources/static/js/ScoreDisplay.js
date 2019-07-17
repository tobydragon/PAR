class ScoreDisplay {
    constructor(scoreObject) {
        this.scoreObject = buildScore(scoreObject);
    }

    buildScore(scoreObject) {
        setCurrentScore(scoreObject);
    }
}

function setCurrentScore(scoreObject) {
    let scoreString = "";
    let scoreType = "ByType";
    let outerNode = document.createElement('div');
    if (scoreType === "ByType") {
        //scoreString=generateScoreByType(scoreObject);
        //scoreString = generateScoreStringByType(scoreObject);
        scoreString = outerNode.appendChild(generateScoreStringByTypeRewritten(scoreObject));
    } else if (scoreType === "SingleScore") {
        scoreString = generateSinglescore(scoreObject);
    } else if (scoreType === "Level") {
        scoreString = generateScoreLevel(scoreObject);
    }
    return scoreString;
}

function generateScoreByType(scoreObject) {
    //80-100 green
    //79-50 orange
    //49-0 red
    var breakdownString = "";
    for (var key in scoreObject) {
        if (scoreObject.hasOwnProperty(key)) {
            let value = scoreObject[key];
            if (value >= 80) {
                breakdownString += "&nbsp <i class=black>" + key + ":</i> <i class=green>" + value + "<i>";
            } else if (value <= 79 && value >= 50) {
                breakdownString += "<i class=black>" + key + ":</i> <i class=orange>" + value + "<i>";
            } else if (value <= 49) {
                breakdownString += "<i class=black>" + key + ":</i> <i class=red>" + value + "<i>";
            }
        }
        breakdownString += "<br />";
    }
    return breakdownString;

}

function generateScoreStringByType(scoreObject) {
    var visString = "";
    for (var key in scoreObject) { //key search
        if (scoreObject.hasOwnProperty(key)) {
            let value = scoreObject[key];
            visString += "&nbsp <i class=black>" + key + ": </i>";
            for (let i = 0; i < value.length; i++) { //value search on string
                if (value[i] == "O") {
                    visString += '<i class="fas fa-check-circle green"></i>';
                } else if (value[i] == "X") {
                    visString += '<i class="fas fa-times-circle red"></i>';
                } else if (value[i] == "_") {
                    visString += '<i class="fas fa-circle"></i>';
                } else {
                    visString += '<i class="fas fa-minus-circle yellow"></i>';
                }
            }
            visString += '<br />';
        }
    }
    return visString;
}

function generateScoreStringByTypeRewritten(scoreObject) {
    let visualBuilder = document.createElement('div');
    console.log("scoreObj: " + JSON.stringify(scoreObject));
    for (let key in scoreObject) {
        if (scoreObject.hasOwnProperty(key)) {
            let value = scoreObject[key];
            visualBuilder.classList.add("black");
            visualBuilder.innerHTML = (key + ": ");
            for (let i = 0; i < value.length; i++) {
                visualBuilder.appendChild(buildVisualSegment(value[i]));
            }
        }
    }

    console.log(visualBuilder);
    return visualBuilder;
}

function buildVisualSegment(value) {
    let childElement = document.createElement('div');
    if (value === 'O') {
        childElement.classList.add("fas");
        childElement.classList.add("fa-check-circle");
        childElement.classList.add("green");
    } else if (value === 'X') {
        childElement.classList.add("fas");
        childElement.classList.add("fa-times-circle");
        childElement.classList.add("red");
    } else if (value === '_') {
        childElement.classList.add("fas");
        childElement.classList.add("fa-circle");
    } else {
        childElement.classList.add("fas");
        childElement.classList.add("fa-minus-circle");
        childElement.classList.add("yellow");
    }
    return childElement;
}
