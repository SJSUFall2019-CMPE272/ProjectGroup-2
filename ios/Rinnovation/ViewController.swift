//
//  ViewController.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/26/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit
import Charts
import MapKit

var city: String?
var renovation: String?

class ViewController: UIViewController {
    var myView: View? {
        view as? View
    }
    var tableViewDelegate: TableViewDelegate?
    
    override func loadView() {
        let view = View()
        view.tableView.register(UITableViewCell.self, forCellReuseIdentifier: tableViewDelegate!.identifier) // TODO: IUO
        view.tableView.dataSource = tableViewDelegate
        view.tableView.delegate = tableViewDelegate
        view.lineChartView.xAxis.axisMinimum = 2008
        view.lineChartView.xAxis.axisMaximum = 2020
        view.lineChartView.xAxis.labelPosition = .bottom
        view.lineChartView.rightAxis.enabled = false
        view.lineChartView.legend.enabled = false
        self.view = view
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        myView?.tableView.reloadData()
        if tableViewDelegate is TableViewDelegate1 {
            if let city = city,
                let renovation = renovation {
                var urlComponents = URLComponents(string: "http://\(host):9000/costs-recouped")
                urlComponents?.queryItems = [
                    URLQueryItem(name: "city", value: city),
                    URLQueryItem(name: "renovation", value: renovation)
                ]
                print(urlComponents!.url!)
                // TODO: IUOs below
                URLSession
                    .shared
                    .dataTask(with: urlComponents!.url!) { (data, _, _) in
                        DispatchQueue.main.async {
                            // TODO: IUO
                            let decoder = JSONDecoder()
                            let entries = try! decoder.decode([CostRecoupedByYear].self, from: data!)
                                .map { ChartDataEntry(x: Double($0.year), y: $0.costRecouped) }
                            let lineChartDataSet = LineChartDataSet(entries: entries)
                            lineChartDataSet.mode = .cubicBezier
                            lineChartDataSet.drawFilledEnabled = true
                            lineChartDataSet.circleColors = Array(repeating: lineChartDataSet.circleColors.first!, count: entries.count - 1) + [.red]
                            self.myView?.lineChartView.data = LineChartData(dataSet: lineChartDataSet)
                        }}
                    .resume()
            }
        } else {
            myView?.lineChartView.isHidden = true
            myView?.tableView.isScrollEnabled = true
        }
    }
}
