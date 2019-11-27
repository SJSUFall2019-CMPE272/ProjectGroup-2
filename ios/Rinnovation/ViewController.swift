//
//  ViewController.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/26/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit
import Charts

var year: String?
var city: String?
var renovation: String?

class ViewController: UIViewController {
    private let identifier = "UITableViewCell"
    private var myView: View? {
        view as? View
    }
    var data = [String]()
    
    override func loadView() {
        let view = View()
        view.tableView.register(UITableViewCell.self, forCellReuseIdentifier: identifier)
        view.tableView.dataSource = self
        view.tableView.delegate = self
        view.tableView.isScrollEnabled = false
        view.lineChartView.xAxis.axisMinimum = 2008
        view.lineChartView.xAxis.axisMaximum = 2019
        view.lineChartView.xAxis.labelPosition = .bottom
        view.lineChartView.rightAxis.enabled = false
        view.lineChartView.legend.enabled = false
//        view.lineChartView.chartDescription?.text = "Cost Recouped by Year"
        self.view = view
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        myView?.tableView.reloadData()
        if let city = city,
            let renovation = renovation {
            var urlComponents = URLComponents(string: "http://localhost:9000/costs-recouped")
            urlComponents?.queryItems = [
                URLQueryItem(name: "city", value: city),
                URLQueryItem(name: "renovation", value: renovation)
            ]
            // TODO: IUOs below
            URLSession
                .shared
                .dataTask(with: urlComponents!.url!) { (data, _, _) in
                    DispatchQueue.main.async {
                        // TODO: IUO
                        let entries = String(data: data!, encoding: .utf8)?.split(separator: ",").enumerated().map { ChartDataEntry(x: Double(2008 + ($0.offset >= 4 ? $0.offset + 1 : $0.offset)), y: Double($0.element.description.trimmingCharacters(in: .whitespacesAndNewlines))!) }
                        let lineChartDataSet = LineChartDataSet(entries: entries)
                        lineChartDataSet.mode = .cubicBezier
                        lineChartDataSet.drawFilledEnabled = true
                        self.myView?.lineChartView.data = LineChartData(dataSet: lineChartDataSet)
                    }
            }
                .resume()
        }
    }
}

// MARK: - UITableViewDataSource

extension ViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        tableView.dequeueReusableCell(withIdentifier: identifier, for: indexPath)
    }
}

// MARK: - UITableViewDelegate

extension ViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        cell.textLabel?.text = data[indexPath.row]
        let label = UILabel()
        label.textColor = .lightGray
        switch data[indexPath.row] {
        case "years": label.text = year
        case "cities": label.text = city
        case "renovations": label.text = renovation
        default: break
        }
        label.sizeToFit()
        cell.accessoryView = label
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if data.count == 3 {
            let viewController = ViewController()
            viewController.data = {
                switch indexPath.row {
                case 0: return (2008...2019).drop { $0 == 2012 }.map(String.init)
                case 1: return ["atlantaga", "sandiegoca"]
                case 2: return ["Bathroom Addition | Midrange", "Kitchen"]
                default: fatalError()
                }
            }()
            navigationController?.pushViewController(viewController, animated: true)
        } else {
            if data.first == "2008" {
                year = tableView.cellForRow(at: indexPath)?.textLabel?.text
            } else if data.first == "atlantaga" {
                city = tableView.cellForRow(at: indexPath)?.textLabel?.text
            } else {
                renovation = tableView.cellForRow(at: indexPath)?.textLabel?.text
            }
            navigationController?.popViewController(animated: true)
        }
    }
}
