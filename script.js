function E(selector, parent) {
    if (selector instanceof HTMLElement)
        return selector;

    return (parent || document).querySelectorAll(selector);
}

function hasClass(element, classname) {
    return element.classList.contains(classname);
}

function radioClass(element, classname) {
    E("." + classname).forEach((elem) =>
        elem.classList.remove(classname)
    );
    element.classList.toggle(classname);
}

// ARRAY GLOBAL DEL CARRITO (fuera de cualquier función)
let carrito = [];

// FUNCIONES DEL CARRITO (independientes de tabs())
function agregarAlCarrito(nombre, precio) {
  carrito.push({ nombre, precio });
  actualizarCarrito();
}

function actualizarCarrito() {
  const carritoItems = document.getElementById('carrito-items');
  const totalElemento = document.getElementById('carrito-total');
  carritoItems.innerHTML = '';
  let total = 0;

  carrito.forEach(item => {
    const li = document.createElement('li');
    li.textContent = `${item.nombre} - $${item.precio}`;
    carritoItems.appendChild(li);
    total += parseInt(item.precio);
  });

  totalElemento.textContent = total;
  // Mostrar el carrito solo si hay items
  document.getElementById('carrito').style.display = carrito.length > 0 ? 'block' : 'none';
}

function finalizarPedido() {
  if (carrito.length === 0) {
    alert('Tu carrito está vacío.');
    return;
  }
  console.log("Pedido:", carrito); // Para depuración
  alert("Gracias por tu pedido. Será preparado pronto.");
  carrito = [];
  actualizarCarrito();
}

document.querySelectorAll('.buy').forEach(boton => {
  boton.addEventListener('click', function () {
    const card = this.closest('.box-1, .box-2, .box-3, .box-4, .box-5, .box-6');
    const nombre = card.querySelector('h3').innerText;
    const precio = card.querySelector('.price').innerText.replace(/\D/g, '');
    agregarAlCarrito(nombre, precio);
  });
});




function tabs(nav) {
    let navElem = E(nav);
    navElem = navElem instanceof HTMLElement ? navElem : navElem[0];

    navElem.addEventListener("click", (e) => {
        let target = e.target;

        if (hasClass(target, "tab"))
            radioClass(target, "active");

        let linkedTab = E("." + target.id);
        if (linkedTab.length > 0)
            radioClass(linkedTab[0], "visible");
    });

    let active = E(".tab.active")[0];
    if (active) {
        radioClass(E("." + active.id)[0], "visible");
    }

    let carrito = [];

function agregarAlCarrito(nombre, precio) {
  carrito.push({ nombre, precio });
  actualizarCarrito();
}

function actualizarCarrito() {
  const carritoItems = document.getElementById('carrito-items');
  const totalElemento = document.getElementById('carrito-total');
  carritoItems.innerHTML = '';
  let total = 0;

  carrito.forEach(item => {
    const li = document.createElement('li');
    li.textContent = `${item.nombre} - $${item.precio}`;
    carritoItems.appendChild(li);
    total += parseInt(item.precio);
  });

  totalElemento.textContent = total;
}

function finalizarPedido() {
  if (carrito.length === 0) {
    alert('Tu carrito está vacío.');
    return;
  }

  console.log("Pedido:", carrito);
  alert("Gracias por tu pedido. Será preparado pronto.");
  carrito = [];
  actualizarCarrito();
}
document.querySelectorAll('.buy').forEach(boton => {
  boton.addEventListener('click', function () {
    const card = this.closest('.card');
    const nombre = card.querySelector('h3').innerText;
    const precio = card.querySelector('.price').innerText.replace('$', '').replace('.', '');

    agregarAlCarrito(nombre, precio);
  });
});



}

tabs(".menu-nav");



// 👇 Agrega desde aquí
document.querySelectorAll('.load-more').forEach((button, index) => {
    button.addEventListener('click', () => {
        const boxContainer = button.previousElementSibling;
        const hiddenBoxes = boxContainer.querySelectorAll('.hidden');

        for (let i = 0; i < 3 && i < hiddenBoxes.length; i++) {
            hiddenBoxes[i].classList.remove('hidden');
        }

        if (boxContainer.querySelectorAll('.hidden').length === 0) {
            button.style.display = 'none';
        }
    });
});




tabs(".menu-nav");

