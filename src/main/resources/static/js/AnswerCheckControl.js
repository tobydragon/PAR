function generateFeedback(isChecked) {
    if (isChecked) {
        return "Correct!";
    } else {
        return "Incorrect. So sad :(";
    }
}

function generateFeedbackQ1(isChecked) {
    if (isChecked) {
        return " Correct";
    } else {
        return " Incorrect";
    }
}

function generateFeedbackQ2(isChecked) {
    if (isChecked) {
        return " Correct";
    } else {
        return " Incorrect";
    }
}

function generateFeedbackQ3(isChecked) {

    if (isChecked) {
        return " Correct";
    } else {
        return " Incorrect";
    }
}

function alternateFeedbackGenerator(isCheckedQ1, isCheckedQ2, isCheckedQ3) {

    var displayStringQ1 = generateFeedbackQ1(isCheckedQ1);
    var displayStringQ2 = generateFeedbackQ2(isCheckedQ2);
    var displayStringQ3 = generateFeedbackQ3(isCheckedQ3);

    var right = ' Correct';
    var wrong = ' Incorrect';

    document.getElementById("statusOne").innerHTML = displayStringQ1;
    document.getElementById("statusTwo").innerHTML = displayStringQ2;
    document.getElementById("statusThree").innerHTML = displayStringQ3;


}

function generateFeedbackMulti(isCheckedQ1, isCheckedQ2, isCheckedQ3) {
    var count = 0;
    if (isCheckedQ1) {
        count++;
    }
    if (isCheckedQ2) {
        count++;
    }
    if (isCheckedQ3) {
        count++
    }
    if (count == 3) {
        return "Correct!";
    } else {
        return "Incorrect. So sad :(";
    }
}

function generateFeedbackPerQuestion(isCheckedQ1, isCheckedQ2, isCheckedQ3) {
    var outputString = "";
    if (isCheckedQ1 && isCheckedQ2 && isCheckedQ3) {
        outputString = "All three correct!";
        return outputString;
    } else if (isCheckedQ1 && isCheckedQ2) {
        outputString = "Question 3 is wrong";
        return outputString;
    } else if (isCheckedQ1 && isCheckedQ3) {
        outputString = "Question 2 is wrong";
        return outputString;
    } else if (isCheckedQ2 && isCheckedQ3) {
        outputString = "Question 1 is wrong";
        return outputString;
    } else if (isCheckedQ1) {
        outputString = "Questions 2 and 3 are wrong";
        return outputString;
    } else if (isCheckedQ2) {
        outputString = "Questions 1 and 3 are wrong";
        return outputString;
    } else if (isCheckedQ3) {
        outputString = "Questions 1 and 2 are wrong";
        return outputString;
    } else {
        outputString = "All three are wrong.";
        return outputString;
    }

}


function displayFeedback(displayString) {
    document.getElementById('answer').innerHTML = displayString;
    displayToggableButton(displayString);
}

function displayToggableButton(displayString) {
    var toggableStateButton = document.getElementById("toggableButton");
    if (displayString == "All three correct!") {
        toggableStateButton.style.display = 'block';
    } else {
        toggableStateButton.style.display = 'none';
    }
}
