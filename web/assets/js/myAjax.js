/**
 * User: Silvan
 * Date: 06.05.12
 * Time: 20:21
 */
var xmlhttp

function loadContent(str, target) {
    alert(str);
    alert("Your browser does not support Ajax HTTP");
    xmlhttp = GetXmlHttpObject();

    if (xmlhttp == null) {
        alert("Your browser does not support Ajax HTTP");
        return;
    }

    var url = str;

    alert(str);

    xmlhttp.onreadystatechange = getOutput(target);
    xmlhttp.open("GET", url, true);
    xmlhttp.send(null);
}

function getOutput(target) {
    if (xmlhttp.readyState == 4) {
        document.getElementById(target).innerHTML = xmlhttp.responseText;
    }
}

function GetXmlHttpObject() {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    }
    if (window.ActiveXObject) {
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
    return null;
}