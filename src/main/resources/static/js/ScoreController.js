class ScoreController {
    constructor(scoreObject) {
        this.element = document.createElement('div');
        this.element.appendChild(buildScoreElement(scoreObject));
    }

    replaceScore(scoreObject){
        this.element.removeChild(this.element.firstChild);
        this.element.appendChild(buildScoreElement(scoreObject));
    }
}

function buildScoreElement(scoreObject) {
    let outerNode = document.createElement('div');
    outerNode.appendChild(generateScoreElementByType(scoreObject));
    //could do generateScoreByType
    return outerNode;
}

//for less spectacular, number score display.
function generateScoreByType(scoreObject) {
    let scoreBuilder;
    let outerNode = document.createElement('div');
    outerNode.classList.add("textAlignRight");
    let buildSegment;
    for (let key in scoreObject) {
        scoreBuilder = document.createElement('div');
        if (scoreObject.hasOwnProperty(key)) {
            let value = scoreObject[key];
            scoreBuilder.classList.add("black");
            scoreBuilder.innerHTML = (key + ": ");

            buildSegment = buildScoreSegment(value);
            scoreBuilder.appendChild(buildSegment);
        }
        outerNode.appendChild(scoreBuilder);
    }
    return outerNode;
}


//for visualization w the circle score display
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

//For number score by type
function buildScoreSegment(value) {
    let childElement = document.createElement('i');
    if (value >= 80) {
        childElement.classList.add("green");
    } else if (value <= 79 && value >= 50) {
        childElement.classList.add("orange");
    } else if (value <= 49 && value >= 1) {
        childElement.classList.add("red");
    } else if (value == -1) {
        value = '';
    }
    childElement.innerHTML = value;
    return childElement;
}

//for visual score by type
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
