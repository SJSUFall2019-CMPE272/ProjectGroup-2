//
//  TableViewDelegate1.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/28/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class TableViewDelegate1: TableViewDelegate {}

// MARK: - UITableViewDataSource

extension TableViewDelegate1 {
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        tableView.dequeueReusableCell(withIdentifier: identifier, for: indexPath)
    }
}

// MARK: - UITableViewDelegate

extension TableViewDelegate1 {
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        cell.textLabel?.text = data[indexPath.row]
        let label = UILabel()
        label.textColor = .lightGray
        label.text = {
            switch indexPath.row {
            case 0: return city.flatMap { englishNamesById[$0] }
            case 1: return renovation
            default: fatalError()
            }
        }()
        label.sizeToFit()
        cell.accessoryView = label
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let nextViewController: UIViewController
        switch indexPath.row {
        case 0:
            nextViewController = TabBarController()
        case 1:
            let viewController = ViewController()
            let tableViewDelegate = TableViewDelegate3()
            tableViewDelegate.viewController = viewController
            viewController.tableViewDelegate = tableViewDelegate
            nextViewController = viewController
        default:
            fatalError()
        }
        viewController?.navigationController?.pushViewController(nextViewController, animated: true)
    }
}
