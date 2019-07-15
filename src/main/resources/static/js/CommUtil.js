//grabbed from : https://stackoverflow.com/questions/19706046/how-to-read-an-external-local-json-file-in-javascript
// function readJsonAsync(file, callbackWithResultingObject) {
//     let rawFile = new XMLHttpRequest();
//     rawFile.overrideMimeType("application/json");
//     rawFile.open("GET", file, true);
//     rawFile.onreadystatechange = function() {
//         if (rawFile.readyState === 4 && rawFile.status == "200") {
//             let resultingObject = JSON.parse(rawFile.responseText);
//             callbackWithResultingObject(resultingObject);
//         }
//     };
//     rawFile.send(null);
// }

function readJson(url) {
    let request = new XMLHttpRequest();
    request.open("GET", url, false);
    request.send(null);
    return JSON.parse(request.response);
}
