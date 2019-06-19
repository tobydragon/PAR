var responsesGivenText = [];


var QuestionTypes = [];
var QuestionAnswers = [];

function getResponsesText() {
    return responsesGivenText;
}


function setQuestionLists() {
    QuestionTypes = getQuestionTypes();
    QuestionAnswers = getQuestionAnswers();
}

function remove() {
    return " ";
}

function clearPage() {
    document.getElementById('plane').innerHTML = remove();
    document.getElementById('structure0').innerHTML = remove();
    document.getElementById('structure1').innerHTML = remove();
    document.getElementById('structure2').innerHTML = remove();
    document.getElementById('structure3').innerHTML = remove();
    document.getElementById('attachment0').innerHTML = remove();
    document.getElementById('attachment1').innerHTML = remove();
    document.getElementById('attachment2').innerHTML = remove();
    document.getElementById('attachment3').innerHTML = remove();
    document.getElementById('zone').innerHTML = remove();


    document.getElementById('planeCorrect').innerHTML = remove();
    document.getElementById('structure0Correct').innerHTML = remove();
    document.getElementById('structure1Correct').innerHTML = remove();
    document.getElementById('structure2Correct').innerHTML = remove();
    document.getElementById('structure3Correct').innerHTML = remove();
    document.getElementById('attachment0Correct').innerHTML = remove();
    document.getElementById('attachment1Correct').innerHTML = remove();
    document.getElementById('attachment2Correct').innerHTML = remove();
    document.getElementById('attachment3Correct').innerHTML = remove();
    document.getElementById('zoneCorrect').innerHTML = remove();
}

function checkAndRecordAnswers(form) {
    for (var i = 0; i < QuestionTypes.length; i++) {
        var currentType = QuestionTypes[i];
        var isCorrect;

        if (currentType == "plane") {
            responsesGivenText.push(form.plane.value);
            if (form.plane.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('planeCorrect').innerHTML = displayCheck(isCorrect);
        } else if (currentType == "zone") {
            responsesGivenText.push(form.zone.value);
            if (form.zone.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('zoneCorrect').innerHTML = displayCheck(isCorrect);

        } else if (currentType == "structure0") {
            responsesGivenText.push(form.structure0.value);
            if (form.structure0.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('structure0Correct').innerHTML = displayCheck(isCorrect);

        } else if (currentType == "structure1") {
            responsesGivenText.push(form.structure1.value);
            if (form.structure1.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('structure1Correct').innerHTML = displayCheck(isCorrect);


        } else if (currentType == "structure2") {
            responsesGivenText.push(form.structure2.value);
            if (form.structure2.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('structure2Correct').innerHTML = displayCheck(isCorrect);

        } else if (currentType == "structure3") {
            responsesGivenText.push(form.structure3.value);
            if (form.structure3.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('structure3Correct').innerHTML = displayCheck(isCorrect);

        } else if (currentType == "attachment0") {
            responsesGivenText.push(form.attachment0.value);
            if (form.attachment0.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('attachment0Correct').innerHTML = displayCheck(isCorrect);


        } else if (currentType == "attachment1") {
            responsesGivenText.push(form.attachment1.value);
            if (form.attachment1.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('attachment1Correct').innerHTML = displayCheck(isCorrect);

        } else if (currentType == "attachment2") {
            responsesGivenText.push(form.attachment2.value);
            if (form.attachment2.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('attachment2Correct').innerHTML = displayCheck(isCorrect);

        } else if (currentType == "attachment3") {
            responsesGivenText.push(form.attachment3.value);
            if (form.attachment3.value == QuestionAnswers[i]) {
                isCorrect = "Correct";
            } else {
                isCorrect = "Incorrect";
            }
            document.getElementById('attachment3Correct').innerHTML = displayCheck(isCorrect);
        }
    }
}

function displayCheck(value) {
    if (value == "Correct") return '<font color=\"green\">Your answer is: ' + value + '</font>';
    if (value == "Incorrect") return '<font color=\"red\">Your answer is: ' + value + '</font>';
    if (value == "Unsure") return '<font color=\"#663399\">Your answer is: ' + value + '</font>';
}

function toggleShowState(toggableElement) {
    var changeElement = document.getElementById(toggableElement).classList;

    if (changeElement.contains("show") || changeElement.style.display == "block") {
        changeElement.remove("show");
        changeElement.add("hide");
    } else if (changeElement.contains("hide")) {
        changeElement.remove("hide");
        changeElement.add("show");
    }
}

function checkAnswers(form) {
    setQuestionLists();
    clearPage();
    checkAndRecordAnswers(form);
    generateResponseJSON();
}
