//
//  DescriptionViewController.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 12/4/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class DescriptionViewController: UIViewController {
    let descriptionView = DescriptionView()
}

// MARK: - UIViewController

extension DescriptionViewController {
    override func loadView() {
        view = descriptionView
    }
}
