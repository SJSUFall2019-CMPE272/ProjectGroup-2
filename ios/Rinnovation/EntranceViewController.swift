//
//  EntranceViewController.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 12/4/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class EntranceViewController: UIViewController {
    @objc func enter() {
        let viewController = ViewController()
        viewController.title = "Rinnovation"
        let tableViewDelegate1 = TableViewDelegate1()
        tableViewDelegate1.data = ["Cities", "Renovations"]
        tableViewDelegate1.viewController = viewController
        viewController.tableViewDelegate = tableViewDelegate1
        UIApplication.shared.windows.first?.rootViewController = UINavigationController(rootViewController: viewController)
    }
}

// MARK: - UIViewController

extension EntranceViewController {
    override func loadView() {
        let entranceView = EntranceView()
        entranceView.enterButton.addTarget(self, action:#selector(enter), for: .touchUpInside)
        view = entranceView
    }
}
