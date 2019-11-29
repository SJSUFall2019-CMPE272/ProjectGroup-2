//
//  TableViewDelegate.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/28/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class TableViewDelegate: NSObject {
    let identifier = "UITableViewCell"
    var data = [String]()
    var viewController: ViewController?
}

extension TableViewDelegate: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        fatalError("Subclass must override")
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        fatalError("Subclass must override")
    }
}

extension TableViewDelegate: UITableViewDelegate {}
