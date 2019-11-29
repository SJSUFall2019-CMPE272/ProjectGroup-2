//
//  SceneDelegate.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/26/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

let developing = true
let host = developing ? "localhost" : "52.53.246.201"

class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?
    
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        let window = UIWindow(frame: windowScene.coordinateSpace.bounds)
        window.windowScene = windowScene
        let viewController = ViewController()
        viewController.title = "Rinnovation"
        let tableViewDelegate1 = TableViewDelegate1()
        tableViewDelegate1.data = ["Cities", "Renovations"]
        tableViewDelegate1.viewController = viewController
        viewController.tableViewDelegate = tableViewDelegate1
        window.rootViewController = UINavigationController(rootViewController: viewController)
        self.window = window
        window.makeKeyAndVisible()
    }
}
