window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}

ajax = function (data, success) {
    $.ajax({
        type: "POST",
        dataType: "json",
        data,
        success
    })
}
