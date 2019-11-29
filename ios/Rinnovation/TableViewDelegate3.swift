//
//  TableViewDelegate3.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/28/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class TableViewDelegate3: TableViewDelegate {
    override init() {
        super.init()
        var urlComponents = URLComponents(string: "http://\(host):9000/renovations")
        urlComponents?.queryItems = city.map { [URLQueryItem(name: "city", value: $0)] }
        print(urlComponents!.url!)
        // TODO: IUOs below
        URLSession
            .shared
            .dataTask(with: urlComponents!.url!) { (dataOptional, _, _) in
                DispatchQueue.main.async {
                    self.data = try! JSONDecoder().decode([String].self, from: dataOptional!)
                    self.viewController?.myView?.tableView.reloadData()
                }}
            .resume()
    }
}

// MARK: - UITableViewDataSource

extension TableViewDelegate3 {
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        tableView.dequeueReusableCell(withIdentifier: identifier, for: indexPath)
    }
}

// MARK: - UITableViewDelegate

extension TableViewDelegate3 {
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        cell.textLabel?.text = data[indexPath.row]
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        renovation = tableView.cellForRow(at: indexPath)?.textLabel?.text
        viewController?.navigationController?.popViewController(animated: true)
    }
}
