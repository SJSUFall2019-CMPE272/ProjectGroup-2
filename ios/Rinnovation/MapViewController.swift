//
//  MapViewController.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/29/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import MapKit

class MapViewController: UIViewController {
    let mapView = MapView()
}

// MARK: - UIViewController

extension MapViewController {
    override func loadView() {
        super.loadView()
        view = mapView
        mapView.mapView.register(MKMarkerAnnotationView.self, forAnnotationViewWithReuseIdentifier: "")
        mapView.mapView.delegate = self
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        mapView.mapView.addAnnotations(cities.map(CityAnnotation.init))
    }
}

// MARK: - MKMapViewDelegate

extension MapViewController: MKMapViewDelegate {
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        guard let cityAnnotation = annotation as? CityAnnotation,
            let markerAnnotationView = mapView.dequeueReusableAnnotationView(withIdentifier: "", for: annotation) as? MKMarkerAnnotationView
            else { fatalError() }
        if let costRecouped = cityAnnotation.city.costRecouped {
            markerAnnotationView.glyphText = "\(Int(costRecouped))"
        }
        return markerAnnotationView
    }
    
    func mapView(_ mapView: MKMapView, didSelect annotationView: MKAnnotationView) {
        guard let cityAnnotation = annotationView.annotation as? CityAnnotation else { fatalError() }
        city = cityAnnotation.city.id
        navigationController?.popViewController(animated: true)
    }
}
