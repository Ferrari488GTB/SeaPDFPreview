//
//  PDFViewController.m
//  HelloIOS
//
//  Created by administrator on 2017/3/29.
//  Copyright © 2017年 administrator. All rights reserved.
//

#import "PDFViewController.h"


@implementation PDFViewController

//隐藏顶部状态栏
-(BOOL)prefersStatusBarHidden{
    return YES;
}

-(instancetype)initWithType:(NSString *)type filePath:(NSString *)filePath fileName:(NSString *)fileName{
    self = [super init];
    if(self!=nil){
        _type = type;
        _filePath = filePath;
        _fileName = fileName;
    }
    return self;
}

-(void)viewDidLoad{
    _pdfWebView = [[UIWebView alloc]initWithFrame:self.view.bounds];
    [self.view addSubview:_pdfWebView];
    _pdfWebView.delegate = self;
    if([_type isEqualToString:@"local"]){
        _url = [NSURL fileURLWithPath:[[NSBundle mainBundle]pathForResource:_fileName ofType:@"pdf" inDirectory:_filePath]];
    }else{
        _url = [NSURL URLWithString:_filePath];
    }
    _request = [NSURLRequest requestWithURL:_url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:30];
    [_pdfWebView loadRequest:_request];
}

-(void)webViewDidStartLoad:(UIWebView *)webView{
    NSLog(@"UIWebView事件: webViewDidStartLoad");
    [self startAnimate];
}

-(void)webViewDidFinishLoad:(UIWebView *)webView{
    NSLog(@"UIWebView事件: webViewDidFinishLoad");
    [self endAnimate];
    [self appendBtns];
}

-(void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error{
    NSLog(@"UIWebView事件: webView");
    NSLog(@"出错了: %@",error);
    [self endAnimate];
    [self appendBtns];
}

-(void)startAnimate{
    _maskView = [[UIView alloc]initWithFrame:self.view.bounds];
    [_maskView setBackgroundColor:[UIColor blackColor]];
    [_maskView setAlpha:0.7];
    [self.view addSubview:_maskView];
    
    _indicatorView = [[UIActivityIndicatorView alloc]initWithFrame:CGRectMake(0.0, 0.0, 48, 48)];
    [_indicatorView setCenter:_maskView.center];
    [_indicatorView setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhite];
    [_maskView addSubview:_indicatorView];
    [_indicatorView startAnimating];
}

-(void)endAnimate{
    [_indicatorView stopAnimating];
    [_maskView removeFromSuperview];
}

-(void)appendBtns{
    // 关闭按钮
    _closeBtn = [[UIButton alloc]initWithFrame:CGRectMake(5, 5, 36, 20)];
    [_closeBtn setBackgroundColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:0.5]];
    [_closeBtn setTitle:@"关闭" forState:UIControlStateNormal];
    [_closeBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    _closeBtn.titleLabel.font = [UIFont systemFontOfSize:12];
    [_closeBtn addTarget:self action:@selector(closePDFPreview) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_closeBtn];
}

-(void)closePDFPreview{
    NSLog(@"关闭PDF预览");
    UIViewController *mainController = [[[[UIApplication sharedApplication]delegate]window]rootViewController];
    [mainController dismissViewControllerAnimated:YES completion:^(){
        NSLog(@"关闭PDF预览视图");
    }];
}

@end

