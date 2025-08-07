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

let carrito = [];
let instrucciones = {};

function agregarAlCarrito(nombre, precio) {
  precio = parseFloat(precio);
  const item = carrito.find(p => p.nombre === nombre);
  if (item) {
    item.cantidad++;
  } else {
    carrito.push({ nombre, precio, cantidad: 1 });
  }
  actualizarCarrito();
}

function cambiarCantidad(nombre, cambio) {
  const item = carrito.find(p => p.nombre === nombre);
  if (!item) return;
  item.cantidad += cambio;
  if (item.cantidad <= 0) {
    carrito = carrito.filter(p => p.nombre !== nombre);
    delete instrucciones[nombre];
  }
  actualizarCarrito();
}

function eliminarProducto(nombre) {
  carrito = carrito.filter(p => p.nombre !== nombre);
  delete instrucciones[nombre];
  actualizarCarrito();
}

function editarPedido(nombre) {
  const input = prompt(`Agrega instrucciones especiales para ${nombre}:`, instrucciones[nombre] || "");
  if (input !== null) instrucciones[nombre] = input.trim();
  actualizarCarrito();
}

function actualizarCarrito() {
  const contenedor = document.getElementById('carrito-items');
  const totalProductosEl = document.getElementById('total-productos');
  const descuentoEl = document.getElementById('descuentos');
  const subtotalEl = document.getElementById('subtotal');

  contenedor.innerHTML = '';
  let total = 0;

  carrito.forEach(item => {
    const itemTotal = item.precio * item.cantidad;
    total += itemTotal;

    const li = document.createElement('li');
    li.className = 'carrito-item';
    li.innerHTML = `
      <div><strong>${item.nombre}</strong> (${item.cantidad})</div>
      <div>Unitario: $${item.precio.toFixed(2)}</div>
      <div>Total: $${itemTotal.toFixed(2)}</div>
      <div class="acciones">
        <button onclick="cambiarCantidad('${item.nombre}', -1)">➖</button>
        <button onclick="cambiarCantidad('${item.nombre}', 1)">➕</button>
        <button onclick="editarPedido('${item.nombre}')">Editar</button>
        <button onclick="eliminarProducto('${item.nombre}')">🗑</button>
      </div>
      ${instrucciones[item.nombre] ? `<div class="nota">📝 ${instrucciones[item.nombre]}</div>` : ''}
    `;
    contenedor.appendChild(li);
  });

  const descuento = total * 0.;
  const subtotal = total - descuento;

  totalProductosEl.textContent = total.toFixed(2);
  descuentoEl.textContent = descuento.toFixed(2);
  subtotalEl.textContent = subtotal.toFixed(2);

  document.getElementById('carrito').style.display = carrito.length > 0 ? 'block' : 'none';
}

function finalizarPedido() {
  if (carrito.length === 0) {
    alert("Tu carrito está vacío.");
    return;
  }

  console.log("Pedido finalizado:", carrito, instrucciones);
  alert("¡Gracias por tu pedido!");
  carrito = [];
  instrucciones = {};
  actualizarCarrito();
}

function E(selector, parent) {
  if (selector instanceof HTMLElement) return selector;
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
}

document.addEventListener('DOMContentLoaded', function () {
  document.querySelectorAll('.buy').forEach(boton => {
    boton.addEventListener('click', function () {
      const card = this.closest('.box-1, .box-2, .box-3, .box-4, .box-5, .box-6');
      const nombre = card.querySelector('h3').innerText;
      const precio = card.querySelector('.price').innerText.replace(/\D/g, '');
      agregarAlCarrito(nombre, precio);
    });
  });

  tabs(".menu-nav");
});

// Mostrar formulario cuando se hace clic en el botón "Domicilio"
document.querySelector('.btn-2')?.addEventListener('click', function (e) {
  e.preventDefault();
  document.getElementById('formulario-domicilio').style.display = 'block';
  document.querySelector('main')?.scrollIntoView({ behavior: 'smooth' });
});

// Captura y muestra los datos del formulario (por ahora en consola)
document.getElementById("form-domicilio")?.addEventListener("submit", function (e) {
  e.preventDefault();

  const datos = Object.fromEntries(new FormData(this).entries());
  console.log(" Datos del cliente:", datos);

  // Oculta formulario y muestra menú
  document.getElementById('formulario-domicilio').style.display = 'none';
  document.querySelector('main')?.scrollIntoView({ behavior: 'smooth' });
});

document.getElementById('form-domicilio').addEventListener('submit', function (event) {
    event.preventDefault();

    const nombre = this.nombre.value;
    const telefono = this.telefono.value;
    const direccion = this.direccion.value;
    const barrio = this.barrio.value;
    const instrucciones = this.instrucciones.value;
    const pago = this.pago.value;
    const tipoPedido = this.tipo_pedido.value;
    const mesa = this.mesa ? this.mesa.value : null;

    const datos = {
        nombre,
        telefono,
        direccion,
        barrio,
        instrucciones,
        pago,
        tipoPedido,
        mesa
    };

    fetch('http://localhost:4567/guardarPedido', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(datos)
    })
    .then(res => res.text())
    .then(mensaje => {
        alert('Pedido enviado con éxito');
        console.log(mensaje);
        this.reset(); // Opcional: limpia el formulario tras envío
        document.getElementById('formulario-domicilio').style.display = 'none'; // Oculta formulario
    })
    .catch(err => {
        console.error('Error al enviar pedido:', err);
    });
});

function seleccionarTipoPedido(tipo) {
  const formulario = document.getElementById("formulario-domicilio");
  const formElement = document.getElementById("form-domicilio");
  const campoMesa = document.getElementById("campo-mesa");
  const tipoPedido = document.getElementById("tipo-pedido");

  // Mostrar el formulario
  formulario.style.display = "block";

  // ✅ Limpiar el formulario cada vez que se cambia de tipo
  formElement.reset();

  if (tipo === "comer_aca") {
    campoMesa.style.display = "block";
    tipoPedido.value = "comer_aca";
    formulario.classList.add("formulario-comer-aqui");
    formulario.classList.remove("formulario-domicilio");
  } else if (tipo === "domicilio") {
    campoMesa.style.display = "none";
    tipoPedido.value = "domicilio";
    formulario.classList.add("formulario-domicilio");
    formulario.classList.remove("formulario-comer-aqui");
  }
}



tabs(".menu-nav");

