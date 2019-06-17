var amountOfStructures=0;
var amountOfAttachments=0;

var QuestionTypes= [];
var QuestionAnswers= [];


function readJson(url){
    var request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);

    return JSON.parse(request.response);
}

function readQuestion(question){

    if(question.difficulty==1) {
        document.getElementById('plane').innerHTML = setPlane(question.correctAnswer);
        QuestionTypes.push("plane")
        QuestionAnswers.push(question.correctAnswer)
    }

    if(question.difficulty==2) {
        if (amountOfStructures != 0) {
            document.getElementById('structure' + amountOfStructures).innerHTML = createFillIn(question);
        } else {
            document.getElementById('structure0').innerHTML = createFillIn(question);
        }
        QuestionTypes.push('structure' + amountOfStructures)
        QuestionAnswers.push(question.correctAnswer)
        amountOfStructures += 1;
    }

    if(question.difficulty==3) {
        if (amountOfAttachments != 0) {
            document.getElementById('attachment' + amountOfAttachments).innerHTML = createFillIn(question);
        } else {
            document.getElementById('attachment0').innerHTML = createFillIn(question);
        }
        QuestionTypes.push('attachment' + amountOfStructures)
        QuestionAnswers.push(question.correctAnswer)
        amountOfAttachments += 1;
    }

    if(question.difficulty==4) {
        document.getElementById('zone').innerHTML = createZoneQuestion(question);
        QuestionTypes.push("zone")
        QuestionAnswers.push(question.correctAnswer)
    }
}

function remove() {
    return " ";
}

function clearpage(){
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

function setPlane(type){
    if(type=="Transverse") return '<p>What plane is this?</p> <input type="radio" name="plane" value="Incorrect"> Lateral<br> <input type="radio" name="plane" value="Correct"> Transverse<br> <input type="radio" name="plane" value="Unsure"> I do not know<br>';
    return '<p>What plane is this?</p> <input type="radio" name="plane" value="Correct"> Lateral<br> <input type="radio" name="plane" value="Incorrect"> Transverse<br> <input type="radio" name="plane" value="Unsure"> I do not know<br>';

}

function createZoneQuestion(json) {
    var question= "<p> What zone is this? </p>";
    return "<p>" + json.questionText + '</p> <input type="radio" value="Incorrect">' + json.answers[0] +
        '<br> <input type="radio" name="plane" value="Correct">' + json.answers[1] +
        '<br> <input type="radio" name="plane" value="Incorrect">' + json.answers[2] +
        '<br> <input type="radio" name="plane" value="Incorrect">' + json.answers[3] + '<br>';
}

function createFillIn(json) {
    return "<p>" + json.questionText + '</p> <input name="structures" list="structures"/> <datalist id="structures"> <option value="' +
        json.answers[0] + '"/> <option value="' + json.answers[1] + '"/> <option value="' + json.answers[2] +
        '"/> <option value="' + json.answers[3] + '"/> </datalist>';
}

// function reEnableSubmit() {
//     document.getElementById('submitButton').innerHTML = '<button type="button" onclick="checkAnswers(form)">\n' + 'Submit\n' + '</button>';
// }

// function changeImage() {
//     var x = document.getElementById("image2");
//
//     if (x.style.display === "none") {
//         x.style.display = "block";
//     } else {
//         x.style.display = "none";
//     }
// }

function changeQuestions() {
    // document.getElementById('image').innerHTML = remove();
    // changeImage();
    clearpage();

    var question = readJson("api/nextQuestion")
    for (var i = 0; i < question.length; i++) readQuestion(question.get(i));

    reEnableSubmit();
}

// function displayCheck(value) {
//     if (value == "Correct") return '<font color=\"green\">Your answer is: ' + value + '</font>';
//     if (value == "Incorrect") return '<font color=\"red\">Your answer is: ' + value + '</font>';
//     if (value == "Unsure") return '<font color=\"#663399\">Your answer is: ' + value + '</font>';
// }
//
// function checkStructure(form) {
//     if (form.structures.value == "Bone") return "Correct";
//     if (form.structures.value == "Unsure") return "Unsure";
//     return "Incorrect";
// }
//
// function checkDistal(form) {
//     if (form.attachment1.value == "Bone") return "Correct";
//     if (form.attachment1.value == "Unsure") return "Unsure";
//     return "Incorrect";
// }
//
// function checkProximal(form) {
//     if (form.attachment0.value == "Bone") return "Correct";
//     if (form.attachment0.value == "Unsure") return "Unsure";
//     return "Incorrect";
// }
//
// function checkAnswers(form) {
//     document.getElementById('planeCorrect').innerHTML = displayCheck(form.plane.value);
//     document.getElementById('structure0Correct').innerHTML = displayCheck(checkStructure(form));
//     document.getElementById('attachment0Correct').innerHTML = displayCheck(checkProximal(form));
//     document.getElementById('attachment1Correct').innerHTML = displayCheck(checkDistal(form))
//     document.getElementById('zoneCorrect').innerHTML = displayCheck(form.zone.value);
//
//     document.getElementById('submitButton').innerHTML = remove();
// }
