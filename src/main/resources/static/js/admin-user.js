const countEl = document.getElementById('selectedCount');

function checks() {
    return Array.from(document.querySelectorAll('.rowCheck'));
}

function updateCount() {
    const selected = checks().filter(c => c.checked).length;
    if (countEl) countEl.textContent = '선택 ' + selected + '명';
}

function toggleAll(flag) {
    checks().forEach(c => c.checked = flag);
    const master = document.getElementById('checkAll');
    if (master) master.checked = flag;
    updateCount();
}

function onCheckAll(master) {
    toggleAll(master.checked);
}

document.addEventListener('DOMContentLoaded', updateCount);