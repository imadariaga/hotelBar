package donamayor.hotelbar.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import donamayor.hotelbar.App;
import donamayor.hotelbar.model.Producto;
import donamayor.hotelbar.model.ProductoComprado;
import donamayor.hotelbar.model.ProductoDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class SegundaEscenaController {

    private int currentRow = 0,currentCol=0;///GridPanean libre dagoen lehen posizioa
   
    private static final int MAX_COLS = 2;

    private List<ProductoComprado> productosComprados = new ArrayList<>();///???
    private List<Producto> productos;///hau hemen behar al da?

    @FXML
    private GridPane gridPane;

    @FXML
    private Button btnVinos, btnSnacks, btnBebidas;

    /** Mostramos todos los productos de la base de datos. */
    @FXML
    public void initialize() {
        productos = ProductoDAO.getProductos();

        //Zergatik hau?
        for (Producto p : productos) {
            productosComprados.add(new ProductoComprado(p, 0));
        }

        mostrarProductos(productos);

        btnVinos.setOnAction(e -> mostrarVinos());
        btnSnacks.setOnAction(e -> mostrarSnacks());
        btnBebidas.setOnAction(e -> mostrarBebidas());
    }

    private void mostrarProductos(List<Producto> productosFiltrados) {
        gridPane.getChildren().clear();
        currentRow = 0;
        currentCol = 0;

        for (Producto p : productosFiltrados) {
            crearHboxDeProducto(p);
        }
    }

    private void crearHboxDeProducto(Producto p) {
        HBox hBoxDeProducto = new HBox(10);
        hBoxDeProducto.setAlignment(Pos.CENTER);
        hBoxDeProducto.setPadding(new Insets(10, 20, 10, 20));

        Label lblNombre = new Label(p.getNombre_es());
        lblNombre.setFont(new Font("PT Sans", 30));
        Label lblPrecio = new Label(String.valueOf(p.getPrecio()) + Currency.getInstance("EUR").getSymbol());
        lblPrecio.setFont(new Font("PT Sans", 18));

        // Buscar la cantidad actual del producto
        /*** Hau utzi dezagun geroko 
        ProductoComprado productoComprado = productosComprados.stream()
                .filter(pc -> pc.getProducto().equals(p))
                .findFirst()
                .orElse(new ProductoComprado(p, 0));
        Label lblCantidad = new Label(String.valueOf(productoComprado.getCantidad()));
        lblCantidad.setFont(new Font("PT Sans", 30));
*/
        Button btnMas = new Button("+");
        Button btnMenos = new Button("-");

        Region espacio = new Region();
        espacio.setPrefWidth(10);

    /***     btnMas.setOnAction(actionEvent -> {
            int cantidad = Integer.parseInt(lblCantidad.getText());
            lblCantidad.setText(String.valueOf(cantidad + 1));
            actualizarCantidadProducto(p, cantidad + 1);
        });
        

        btnMenos.setOnAction(actionEvent -> {
            int cantidad = Integer.parseInt(lblCantidad.getText());
            if (cantidad > 0) {
                lblCantidad.setText(String.valueOf(cantidad - 1));
                actualizarCantidadProducto(p, cantidad - 1);
            }
        });
*/
        hBoxDeProducto.getChildren().addAll(lblNombre, lblPrecio, espacio, btnMenos, btnMas);

        gridPane.add(hBoxDeProducto, currentCol, currentRow);

        currentCol++;
        if (currentCol >= MAX_COLS) {
            currentCol = 0;
            currentRow++;
        }
    }

    private void actualizarCantidadProducto(Producto producto, int nuevaCantidad) {
        for (ProductoComprado productoComprado : productosComprados) {
            if (productoComprado.getProducto().equals(producto)) {
                productoComprado.setCantidad(nuevaCantidad);
                break;
            }
        }
    }

    @FXML
    private void mostrarVinos() {
        List<Producto> vinos = ProductoDAO.getProductosPorTipo("Vino");
        mostrarProductos(vinos);
    }

    @FXML
    private void mostrarSnacks() {
        List<Producto> snacks = ProductoDAO.getProductosPorTipo("Snack");
        mostrarProductos(snacks);
    }

    @FXML
    private void mostrarBebidas() {
        List<Producto> bebidas = ProductoDAO.getProductosPorTipo("Bebida");
        mostrarProductos(bebidas);
    }

    @FXML
    private void carrito() throws IOException {
        App.setProductosComprados(productosComprados);
        App.setRoot("carrito");
    }

    @FXML
    void atras() throws IOException {
        App.setRoot("primeraescena");
    }
}
