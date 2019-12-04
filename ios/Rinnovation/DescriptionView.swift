//
//  DescriptionView.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 12/4/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class DescriptionView: UIView {
    let descriptionTextView: UITextView = {
        $0.backgroundColor = .clear
        $0.font = .systemFont(ofSize: 14)
        $0.textAlignment = .center
        $0.textContainer.maximumNumberOfLines = 0
        $0.isEditable = false
        return $0
    }(UITextView())
    let imageView = UIImageView()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = UIColor.white.withAlphaComponent(0.7)
        addSubview(descriptionTextView)
        addSubview(imageView)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

// MARK: - UIView

extension DescriptionView {
    override func layoutSubviews() {
        super.layoutSubviews()
        imageView.frame.size = CGSize(width: frame.width * 0.65, height: frame.height * 0.65)
        imageView.center.x = center.x
        imageView.frame.origin.y = 44

        descriptionTextView.frame.size.width = frame.width * 0.85
        descriptionTextView.sizeToFit()
        descriptionTextView.center.x = center.x
        descriptionTextView.frame.origin.y = imageView.frame.maxY + 16
    }
}
