/*
 * Copyright (C) 2015 cesarvefe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.unisabana.dyas.samples.services.client;



import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author cesarvefe
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        SqlSession sqlss = sessionfact.openSession();
        
        edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper cm = sqlss.getMapper(edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper.class);
        edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ItemMapper im = sqlss.getMapper(edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ItemMapper.class);

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("    SISTEMA DE GESTION VÍDEO RENTAL");
            System.out.println("========================================");
            System.out.println("1. Consultar todos los clientes");
            System.out.println("2. Consultar cliente por ID (Documento)");
            System.out.println("4. Agregar Item rentado a Cliente (Prueba)");
            System.out.println("5. Insertar nuevo Item (Prueba)");
            System.out.println("0. Salir");
            System.out.print("\nSeleccione una opción: ");
            
            String opcion = scanner.nextLine();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n--- Lista de Clientes ---");
                        System.out.println(cm.consultarClientes());
                        break;
                    case "2":
                        System.out.print("Ingrese el documento del cliente (ej. 123456789): ");
                        int doc = Integer.parseInt(scanner.nextLine());
                        System.out.println("\n--- Información del Cliente ---");
                        System.out.println(cm.consultarCliente(doc));
                        break;
                    case "3":
                        System.out.println("\n--- Lista de Items ---");
                        System.out.println(im.consultarItems());
                        break;
                    case "4":
                        System.out.print("Ingrese Documento del Cliente: ");
                        int docCli = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese ID del Item a rentar: ");
                        int idItem = Integer.parseInt(scanner.nextLine());
                        java.sql.Date fechaInicio = new java.sql.Date(System.currentTimeMillis());
                        java.sql.Date fechaFin = new java.sql.Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3)); // +3 dias
                        cm.agregarItemRentadoACliente(docCli, idItem, fechaInicio, fechaFin);
                        System.out.println("\n[OK] Item rentado agregado exitosamente (fechas automáticas generadas).");
                        break;
                    case "5":
                        System.out.print("Ingrese ID del nuevo Item: ");
                        int newItemId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese nombre del nuevo Item: ");
                        String newNombre = scanner.nextLine();
                        System.out.print("Ingrese ID del Tipo de Item (ej. 1, 2 o 3): ");
                        int idTipo = Integer.parseInt(scanner.nextLine());
                        edu.unisabana.dyas.samples.entities.TipoItem tipo = new edu.unisabana.dyas.samples.entities.TipoItem(idTipo, "N/A");
                        edu.unisabana.dyas.samples.entities.Item nuevoItem = new edu.unisabana.dyas.samples.entities.Item(tipo, newItemId, newNombre, "Descripción generica", new java.sql.Date(System.currentTimeMillis()), 5000, "Diario", "Varios");
                        im.insertarItem(nuevoItem);
                        System.out.println("\n[OK] Item insertado exitosamente.");
                        break;
                    case "0":
                        salir = true;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("\n[ERROR] Ocurrió un problema durante la consulta: " + e.getMessage());
            }
        }
        
        sqlss.commit();
        sqlss.close();
        scanner.close();
    }


}
