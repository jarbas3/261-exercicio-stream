package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import entities.Product;

public class Program {

	public static void main(String[] args) {
	
		Locale.setDefault(Locale.US); // Convenções de formatação de números e datas dos EUA.
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter full file path: ");
		String path = sc.next();
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			
			List<Product> list = new ArrayList<>();
			
			String line = br.readLine();
			
			while (line != null) {
				String[] fields = line.split(",");
				String description = fields[0];
				double price = Double.parseDouble(fields[1]);
				list.add(new Product(description, price));
				line = br.readLine();
			}
			
			// Tirar a média dos preços de todos os produtos da lista.
			double average = list.stream()
					.mapToDouble(Product::getPrice)
					.average()
					.orElse(0.0);
			
			System.out.println("Average price: " + String.format("%.2f", average));
			
			// Filtrar os produtos com preço abaixo da média e ordená-los em ordem alfabética reversa.			
			Collator collator = Collator.getInstance(new Locale("pt", "BR"));
			collator.setStrength(Collator.TERTIARY);
			List<String> names = list.stream()
					.filter(p -> p.getPrice() < average)
					.map(Product::getDescription)
					.sorted(collator.reversed())
					.collect(Collectors.toList());
			
			// Imprimir os nomes dos produtos filtrados e ordenados.
			names.forEach(System.out::println);
		}
		catch (IOException e) {
			e.getStackTrace(); // Exibe o StackTrace para fins de depuração.
		}
		finally {
			if (sc != null) {
				sc.close(); // Encerra o Scanner no bloco finally.
			}			
		}		
	}
}
