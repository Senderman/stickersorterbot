function getCsrfHeaders(){
    csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content')
    csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content')

    headers = {}
    headers['Content-Type'] = 'application/json'
    headers[csrfHeader] = csrfToken

    return headers
}

function setTags(fileUniqueId, tags) {
    data = JSON.stringify({
        fileUniqueId: fileUniqueId,
        tags: tags
    })

    fetch('/setTags', {
        method: 'POST',
        headers: getCsrfHeaders(),
        body: data
    })
    .then(r => {
        if (r.status == 200) {
            alert("Успешно!")
        } else {
            alert("Ошибка!")
        }
    })
}

function deleteSticker(fileUniqueId) {
    data = JSON.stringify({
        fileUniqueId: fileUniqueId
    })

    fetch('/deleteSticker', {
        method: 'POST',
        headers: getCsrfHeaders(),
        body: data
    })
}

function changeTagsBtnClick(btn) {
    stickerDiv = btn.parentElement
    tags = stickerDiv.querySelector('input#tags').value
    fileUniqueId = stickerDiv.querySelector('input#fileUniqueId').value
    setTags(fileUniqueId, tags)
}

function deleteStickerBtnClick(btn) {
    stickerDiv = btn.parentElement
    fileUniqueId = stickerDiv.querySelector('input#fileUniqueId').value
    deleteSticker(fileUniqueId)
    stickerColumn = stickerDiv.parentNode
    stickerColumn.parentNode.removeChild(stickerColumn)
}

