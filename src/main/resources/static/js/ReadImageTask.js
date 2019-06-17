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
            document.getElementById('structure' + amountOfStructures).innerHTML = createFillIn(question, 'structure' + amountOfStructures);
        } else {
            document.getElementById('structure0').innerHTML = createFillIn(question, 'structure0');
        }
        QuestionTypes.push('structure' + amountOfStructures)
        QuestionAnswers.push(question.correctAnswer)
        amountOfStructures += 1;
    }

    if(question.difficulty==3) {
        if (amountOfAttachments != 0) {
            document.getElementById('attachment' + amountOfAttachments).innerHTML = createFillIn(question, 'attachment' + amountOfAttachments );
        } else {
            document.getElementById('attachment0').innerHTML = createFillIn(question,'attachment0' );
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

function setPlane(type){
    if(type=="Transverse") {
        return '<p>What plane is this?</p> <input type="radio" name="plane" value="Incorrect"> Lateral<br> <input type="radio" name="plane" value="Correct"> Transverse<br> <input type="radio" name="plane" value="Unsure"> I do not know<br>';
    } else {
        return '<p>What plane is this?</p> <input type="radio" name="plane" value="Correct"> Lateral<br> <input type="radio" name="plane" value="Incorrect"> Transverse<br> <input type="radio" name="plane" value="Unsure"> I do not know<br>';
    }
}

function createZoneQuestion(json) {
    var question= "<p> What zone is this? </p>";
    for(var i = 0; i < json.answers.length; i++){
        question+='<br> <input type="radio" name="zone" value=';
        if(json.answers[i]==json.correctAnswer){
            question+="Correct";
            question+= json.answers[i];
        } else {
            question+="Inorrect";
            question+= json.answers[i];
        }
    }
    question+= '<br>'
    return question;
}

function createFillIn(json, type) {
    var question= "<p>" + json.questionText + '</p> <input name="'+ type + '" list="'+ type+ '"/> <datalist id="'+ type+'">';
    for(var i = 0; i < json.answers.length; i++){
        question= question +'<option value="' +json.answers[0] + '"/>';
    }
    question+= '</datalist>';

    return question;
}

function changeQuestions() {
    clearpage();

    var question = readJson("api/nextImageTask")
    for (var i = 0; i < question.length; i++) readQuestion(question.get(i));
}