package com.example.bluetoothclassicapp

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class MainActivity : Activity() {

    private lateinit var dataView: TextView
    private lateinit var connectButton: Button

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private val deviceUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID padrão para dispositivos Bluetooth Clássico

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataView = findViewById(R.id.dataView)
        connectButton = findViewById(R.id.connectButton)

        connectButton.setOnClickListener {
            if (checkBluetoothPermissions()) {
                connectToBluetoothDevice()
            } else {
                requestBluetoothPermissions()
            }
        }

        // Pedir permissão de escrita no armazenamento externo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    private fun checkBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Para Android 12 (API 31) e superior
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            // Para versões anteriores
            true
        }
    }

    private fun requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                2
            )
        }
    }

    private fun connectToBluetoothDevice() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não disponível", Toast.LENGTH_SHORT).show()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        if (pairedDevices != null && pairedDevices.isNotEmpty()) {
            val device = pairedDevices.first() // Seleciona o primeiro dispositivo pareado
            connectToDevice(device)
        } else {
            Toast.makeText(this, "Nenhum dispositivo pareado encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        Thread {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(deviceUUID)
                bluetoothSocket?.connect()
                runOnUiThread {
                    Toast.makeText(this, "Conectado a ${device.name}", Toast.LENGTH_SHORT).show()
                }
                readDataFromBluetooth()
            } catch (e: IOException) {
                Log.e("Bluetooth", "Erro ao conectar", e)
            }
        }.start()
    }

    private fun readDataFromBluetooth() {
        val inputStream: InputStream? = bluetoothSocket?.inputStream
        val buffer = ByteArray(1024)
        var bytes: Int

        while (true) {
            try {
                inputStream?.let {
                    bytes = it.read(buffer)
                    val receivedData = String(buffer, 0, bytes)
                    runOnUiThread {
                        dataView.text = receivedData
                        saveToFile(receivedData)
                    }
                } ?: break
            } catch (e: IOException) {
                Log.e("Bluetooth", "Erro ao ler dados", e)
                break
            }
        }
    }

    private fun saveToFile(data: String) {
        val externalStorage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getExternalFilesDir(null)
        } else {
            Environment.getExternalStorageDirectory()
        }
        val file = File(externalStorage, "received_data.txt")

        try {
            val fos = FileOutputStream(file, true)
            fos.write((data + "\n").toByteArray())
            fos.close()
            Log.i("Bluetooth", "Dados salvos em: ${file.absolutePath}")
        } catch (e: IOException) {
            Log.e("Bluetooth", "Erro ao salvar arquivo", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            Log.e("Bluetooth", "Erro ao fechar socket", e)
        }
    }
}
