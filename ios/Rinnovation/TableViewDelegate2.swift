//
//  TableViewDelegate2.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/28/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit
import MapKit

var cities = [City]()
var englishNamesById = [String: String]()

struct City: Codable {
    let id: String
    let englishName: String
    let lat: Double
    let lng: Double
    let costRecouped: Double?
}

class TableViewDelegate2: TableViewDelegate {
    override init() {
        super.init()
        var urlComponents = URLComponents(string: "http://\(host):9000/cities")
        urlComponents?.queryItems = renovation.map { [URLQueryItem(name: "renovation", value: $0)] }
        print(urlComponents!.url!)
        // TODO: IUOs below
        URLSession
            .shared
            .dataTask(with: urlComponents!.url!) { (dataOptional, _, _) in
                DispatchQueue.main.async {
                    cities = try! JSONDecoder().decode([City].self, from: dataOptional!)
                    englishNamesById = cities.reduce(into: [String: String]()) { $0[$1.id] = $1.englishName }
                    self.data = cities.map { $0.id }
                    self.viewController?.myView?.tableView.reloadData()
                }}
            .resume()
    }
}

// MARK: - UITableViewDataSource

extension TableViewDelegate2 {
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        tableView.dequeueReusableCell(withIdentifier: identifier, for: indexPath)
    }
}

// MARK: - UITableViewDelegate

extension TableViewDelegate2 {
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        cell.textLabel?.text = englishNamesById[data[indexPath.row]]
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        city = data[indexPath.row]
        viewController?.navigationController?.popViewController(animated: true)
    }
}
