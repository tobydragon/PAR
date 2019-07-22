class ScoreDisplay {
    constructor(scoreObject) {
        this.scoreObject = buildScore(scoreObject);
    }

    buildScore(scoreObject) {
        setCurrentScore(scoreObject);
    }
}

function setCurrentScore(scoreObject, scoreType) {
    let scoreString = "";
    let outerNode = document.createElement('div');
    if (scoreType === "ByType") {
        //scoreString=generateScoreByType(scoreObject);
        scoreString = outerNode.appendChild(generateScoreElementByType(scoreObject));
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
/**
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
}**/

function generateScoreElementByType(scoreObject) {
    let visualBuilder;
    let outerNode = document.createElement('div');
    outerNode.classList.add("textAlignRight");
    let buildSegment;
    for (let key in scoreObject) {
        visualBuilder = document.createElement('div');
        if (scoreObject.hasOwnProperty(key)) {
            let value = scoreObject[key];
            visualBuilder.classList.add("black");
            visualBuilder.innerHTML = (key + ": ");
            for (let i = 0; i < value.length; i++) {
                buildSegment = buildVisualSegment(value[i]);
                visualBuilder.appendChild(buildSegment);
            }
        }
        outerNode.appendChild(visualBuilder);
    }
    return outerNode;
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
        childElement.classList.add("light-grey");
    } else {
        childElement.classList.add("fas");
        childElement.classList.add("fa-minus-circle");
        childElement.classList.add("yellow");
    }
    return childElement;
}
