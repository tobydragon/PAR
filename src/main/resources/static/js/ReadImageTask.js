var amountOfStructures = 0;
var amountOfAttachments = 0;

var QuestionTypes = [];
var QuestionAnswers = [];

var QuestionIDs = [];

var UserID;

function getUserID() {
    return UserID;
}

function getQuestionTypes() {
    return QuestionTypes;
}

function getQuestionAnswers() {
    return QuestionAnswers;
}

function getQuestionIDs() {
    return QuestionIDs;
}

function readJson(url) {
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}

function readQuestion(question) {
    QuestionIDs.push(question.id);

    if (question.difficulty == 1) {
        document.getElementById('plane').innerHTML = setPlane();
        QuestionTypes.push("plane")
        QuestionAnswers.push(question.correctAnswer)
    }

    if (question.difficulty == 2) {
        if (amountOfStructures != 0) {
            document.getElementById('structure' + amountOfStructures).innerHTML = createFillIn(question, 'structure' + amountOfStructures);
        } else {
            document.getElementById('structure0').innerHTML = createFillIn(question, 'structure0');
        }
        QuestionTypes.push('structure' + amountOfStructures)
        QuestionAnswers.push(question.correctAnswer)
        amountOfStructures += 1;
    }

    if (question.difficulty == 3) {
        if (amountOfAttachments != 0) {
            document.getElementById('attachment' + amountOfAttachments).innerHTML = createFillIn(question, 'attachment' + amountOfAttachments);
        } else {
            document.getElementById('attachment0').innerHTML = createFillIn(question, 'attachment0');
        }
        QuestionTypes.push('attachment' + amountOfStructures)
        QuestionAnswers.push(question.correctAnswer)
        amountOfAttachments += 1;
    }

    if (question.difficulty == 4) {
        document.getElementById('zone').innerHTML = createZoneQuestion(question);
        QuestionTypes.push("zone")
        QuestionAnswers.push(question.correctAnswer)
    }
}

function setPlane() {
    return '<p>What plane is this?</p> <input type="radio" name="plane" value="Lateral"> Lateral<br> <input type="radio" name="plane" value="Transverse"> Transverse<br> <input type="radio" name="plane" value="Unsure"> I do not know<br>';
}

function createZoneQuestion(json) {
    var question = "<p> What zone is this? </p>";
    for (var i = 0; i < json.answers.length; i++) {
        question += '<br> <input type="radio" name="zone" value"';
        question = question + json.answers[i] + '">' + json.answers[i] + '<br>';
    }
    return question;
}

function createFillIn(json, type) {
    var question = "<p>" + json.questionText + '</p> <input name="' + type + '" list="' + type + '"/> <datalist id="' + type + '">';
    for (var i = 0; i < json.answers.length; i++) {
        question = question + '<option value="' + json.answers[i] + '"/>';
    }
    question += '</datalist>';

    return question;
}

function changeQuestions() {
    clearpage();

    var question = readJson("api/nextImageTask")
    for (var i = 0; i < question.length; i++) readQuestion(question.get(i));
}

function displayImageURL(imageURL) {
    return '<img class="imgCenter" src="' + imageURL + '">';
}

function pageDisplay() {
    var i;
    var imageTaskJSON = readJson("api/nextImageTask");
    var qArray = new Array[imageTaskJSON.taskQuestions.length];
    //Displays the image on the page at the appropriate tag
    document.getElementById("image").innerHTML = displayImageURL(imageTaskJSON.taskQuestions[0].imageURL);
    //Displays the questions at the tags
    for (i = 0; i < imageTaskJSON.taskQuestions.length; i++) {
        qArray.add(imageTaskJSON.taskQuestions[i]);
        readQuestion(qArray[i]);
    }
}
