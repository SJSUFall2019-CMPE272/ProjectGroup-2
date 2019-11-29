//
//  View.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/26/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit
import Charts

class View: UIView {
    let tableView = UITableView()
    let lineChartView = LineChartView()
    
    // MARK: - initializers
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = .white
        addSubview(tableView)
        addSubview(lineChartView)
        lineChartView.backgroundColor = .white
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

// MARK: - UIView

extension View {
    override func layoutSubviews() {
        super.layoutSubviews()
        if lineChartView.isHidden {
            tableView.frame = frame
        } else {
            let safeAreaInsets = window?.safeAreaInsets
            let statusBarHeight = safeAreaInsets?.top ?? 0
            let navigationBarHeight = (window?.rootViewController as? UINavigationController)?.navigationBar.frame.height ?? 0
            let tableViewHeight = statusBarHeight + navigationBarHeight + 44 * 2
            tableView.frame.size = CGSize(width: frame.width, height: tableViewHeight)
            let homeBarHeight = safeAreaInsets?.bottom ?? 0
            lineChartView.frame = CGRect(x: 0, y: tableViewHeight, width: frame.width, height: frame.height - tableViewHeight - homeBarHeight)
        }
    }
}
