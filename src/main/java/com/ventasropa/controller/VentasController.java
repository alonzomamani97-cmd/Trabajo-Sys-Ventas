package com.ventasropa.controller;

import com.ventasropa.components.AlertHelper;
import com.ventasropa.config.AppConfig;
import com.ventasropa.dto.ProductoDTO;
import com.ventasropa.enums.Talla;
import com.ventasropa.model.Producto;
import com.ventasropa.service.IProductoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.stream.Collectors;

public class VentasController {

    @FXML private TextField txtNombre;
    @FXML private ComboBox<Talla> cbTalla;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private TextField txtBuscar;

    //herencia borderPane , Vbox , TextFiel , TableView
    @FXML private TableView<Producto> tablaPrendas;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Talla> colTalla;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    //polimorfismo
    private final IProductoService service = AppConfig.productoService;
    private Producto productoSeleccionado;

    @FXML
    public void initialize() {
        cbTalla.setItems(FXCollections.observableArrayList(Talla.values()));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTalla.setCellValueFactory(new PropertyValueFactory<>("talla"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tablaPrendas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                productoSeleccionado = newSelection;
                txtNombre.setText(newSelection.getNombre());
                cbTalla.setValue(newSelection.getTalla());
                txtPrecio.setText(String.valueOf(newSelection.getPrecio()));
                txtStock.setText(String.valueOf(newSelection.getStock()));
                limpiarErroresVisuales();
            }
        });

        txtNombre.textProperty().addListener((obs, oldV, newV) -> txtNombre.getStyleClass().remove("input-error"));
        txtPrecio.textProperty().addListener((obs, oldV, newV) -> txtPrecio.getStyleClass().remove("input-error"));
        txtStock.textProperty().addListener((obs, oldV, newV) -> txtStock.getStyleClass().remove("input-error"));
        cbTalla.valueProperty().addListener((obs, oldV, newV) -> cbTalla.getStyleClass().remove("input-error"));

        actualizarTabla();
    }

    private void limpiarErroresVisuales() {
        txtNombre.getStyleClass().remove("input-error");
        cbTalla.getStyleClass().remove("input-error");
        txtPrecio.getStyleClass().remove("input-error");
        txtStock.getStyleClass().remove("input-error");
    }

    private void marcarComoError(Control control) {
        if (!control.getStyleClass().contains("input-error")) {
            control.getStyleClass().add("input-error");
        }
    }

    private boolean validarCampos() {
        limpiarErroresVisuales();

        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            marcarComoError(txtNombre);
            AlertHelper.mostrarError("Campo Vacío", "Debe ingresar el nombre del producto.");
            txtNombre.requestFocus();
            return false;
        }

        if (cbTalla.getValue() == null) {
            marcarComoError(cbTalla);
            AlertHelper.mostrarError("Campo Vacío", "Debe seleccionar una talla de la lista.");
            cbTalla.requestFocus();
            return false;
        }

        try {
            double precio = Double.parseDouble(txtPrecio.getText());
            if (precio <= 0) {
                marcarComoError(txtPrecio);
                AlertHelper.mostrarError("Valor Inválido", "El precio debe ser mayor a 0.");
                txtPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            marcarComoError(txtPrecio);
            AlertHelper.mostrarError("Formato Incorrecto", "El precio debe ser un número (Ej. 45.50).");
            txtPrecio.requestFocus();
            return false;
        }

        try {
            int stock = Integer.parseInt(txtStock.getText());
            if (stock < 0) {
                marcarComoError(txtStock);
                AlertHelper.mostrarError("Valor Inválido", "El stock no puede ser un número negativo.");
                txtStock.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            marcarComoError(txtStock);
            AlertHelper.mostrarError("Formato Incorrecto", "El stock debe ser un número entero (Ej. 10).");
            txtStock.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    protected void onGuardarClick() {
        if (productoSeleccionado != null) {
            onEditarClick();
            return;
        }

        if (!validarCampos()) return;

        try {
            ProductoDTO dto = new ProductoDTO(txtNombre.getText(), cbTalla.getValue(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtStock.getText()));
            service.agregarProducto(dto);
            limpiarCampos();
            actualizarTabla();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operación Exitosa");
            alert.setHeaderText(null);
            alert.setContentText("El producto se registró correctamente.");
            alert.showAndWait();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    protected void onEditarClick() {
        if (productoSeleccionado != null) {
            if (!validarCampos()) return;

            try {
                ProductoDTO dto = new ProductoDTO(txtNombre.getText(), cbTalla.getValue(),
                        Double.parseDouble(txtPrecio.getText()),
                        Integer.parseInt(txtStock.getText()));

                service.actualizarProducto(productoSeleccionado.getId(), dto);
                limpiarCampos();
                actualizarTabla();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Operación Exitosa");
                alert.setHeaderText(null);
                alert.setContentText("El producto se actualizó correctamente.");
                alert.showAndWait();

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        } else {
            AlertHelper.mostrarError("Acción Requerida", "Seleccione un producto de la tabla para editar.");
        }
    }

    @FXML
    protected void onCancelarClick() {
        limpiarCampos();
    }

    @FXML
    protected void onBuscarClick() {
        String filtro = txtBuscar.getText().toLowerCase();
        if (filtro.isEmpty()) {
            actualizarTabla();
        } else {
            var listaFiltrada = service.listarTodos().stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(filtro))
                    .collect(Collectors.toList());
            tablaPrendas.setItems(FXCollections.observableArrayList(listaFiltrada));
            tablaPrendas.refresh();
        }
    }

    @FXML
    protected void onEliminarClick() {
        if (productoSeleccionado != null) {
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("Confirmar eliminación");
            alertConfirm.setHeaderText(null);
            alertConfirm.setContentText("¿Está seguro de eliminar el producto: " + productoSeleccionado.getNombre() + "?");

            if (alertConfirm.showAndWait().get() == ButtonType.OK) {
                service.eliminarProducto(productoSeleccionado.getId());
                limpiarCampos();
                actualizarTabla();
            }
        } else {
            AlertHelper.mostrarError("Acción Requerida", "Seleccione un producto de la tabla para eliminar.");
        }
    }

    private void actualizarTabla() {
        tablaPrendas.setItems(FXCollections.observableArrayList(service.listarTodos()));
        tablaPrendas.refresh();
    }

    private void limpiarCampos() {
        limpiarErroresVisuales();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtBuscar.clear();
        cbTalla.setValue(null);
        productoSeleccionado = null;
        tablaPrendas.getSelectionModel().clearSelection();
    }
}