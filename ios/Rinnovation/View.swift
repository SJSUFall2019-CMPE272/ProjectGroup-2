//
//  View.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/26/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class View: UIView {
    let tableView = UITableView()
    
    // MARK: - initializers
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        addSubview(tableView)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

// MARK: - UIView

extension View {
    override func layoutSubviews() {
        super.layoutSubviews()
        tableView.frame = frame
    }
}
