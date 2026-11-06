package com.example.cuentadebaradrianluque;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {

    private EditText etTotal;
    private CheckBox cbPropina;
    private SeekBar sbPropina;
    private TextView tvPorcentaje;
    private RadioGroup rgPago;
    private RatingBar rbValoracion;
    private AutoCompleteTextView actvCamarero;
    private Button btnCalcular;
    private TextView tvResultado;

    private String[] camareros = {
            "Carlos",
            "María",
            "Antonio",
            "Lucía",
            "Javier",
            "Sofía",
            "Daniel",
            "Elena"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTotal = findViewById(R.id.etTotal);
        cbPropina = findViewById(R.id.cbPropina);
        sbPropina = findViewById(R.id.sbPropina);
        tvPorcentaje = findViewById(R.id.tvPorcentaje);
        rgPago = findViewById(R.id.rgPago);
        rbValoracion = findViewById(R.id.rbValoracion);
        actvCamarero = findViewById(R.id.actvCamarero);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                camareros
        );

        actvCamarero.setAdapter(adaptador);
        actvCamarero.setThreshold(3);

        sbPropina.setProgress(10);

        sbPropina.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPorcentaje.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnCalcular.setOnClickListener(v -> calcularCuenta());
    }

    private void calcularCuenta() {

        String cantidad = etTotal.getText().toString().trim();

        if (cantidad.isEmpty()) {
            mostrarError(getString(R.string.error_vacio));
            return;
        }

        if (!cantidad.matches("[0-9]+([.,][0-9]{1,2})?")) {
            mostrarError(getString(R.string.error_formato));
            return;
        }

        double total;

        try {
            total = Double.parseDouble(cantidad.replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarError(getString(R.string.error_formato));
            return;
        }

        if (total <= 0) {
            mostrarError(getString(R.string.error_valor));
            return;
        }

        double propina = 0;

        if (cbPropina.isChecked()) {
            propina = total * sbPropina.getProgress() / 100.0;
        }

        double totalFinal = total + propina;

        int idSeleccionado = rgPago.getCheckedRadioButtonId();
        RadioButton metodoSeleccionado = findViewById(idSeleccionado);

        String metodo = metodoSeleccionado.getText().toString();

        float valoracion = rbValoracion.getRating();

        String camarero = actvCamarero.getText().toString().trim();

        if (camarero.isEmpty()) {
            camarero = getString(R.string.sin_camarero);
        }

        NumberFormat formato = NumberFormat.getNumberInstance(Locale.getDefault());
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);

        String resultado = getString(
                R.string.resultado_detalle,
                formato.format(total),
                formato.format(propina),
                formato.format(totalFinal),
                metodo,
                valoracion,
                camarero
        );

        tvResultado.setTextColor(Color.rgb(40, 120, 70));
        tvResultado.setText(resultado);
    }

    private void mostrarError(String mensaje) {
        tvResultado.setTextColor(Color.RED);
        tvResultado.setText(mensaje);
    }
}
