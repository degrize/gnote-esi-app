import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDemandeInspecteurDE, DemandeInspecteurDE } from '../demande-inspecteur-de.model';
import { DemandeInspecteurDEService } from '../service/demande-inspecteur-de.service';

import { DemandeInspecteurDERoutingResolveService } from './demande-inspecteur-de-routing-resolve.service';

describe('DemandeInspecteurDE routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DemandeInspecteurDERoutingResolveService;
  let service: DemandeInspecteurDEService;
  let resultDemandeInspecteurDE: IDemandeInspecteurDE | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(DemandeInspecteurDERoutingResolveService);
    service = TestBed.inject(DemandeInspecteurDEService);
    resultDemandeInspecteurDE = undefined;
  });

  describe('resolve', () => {
    it('should return IDemandeInspecteurDE returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInspecteurDE = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeInspecteurDE).toEqual({ id: 123 });
    });

    it('should return new IDemandeInspecteurDE if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInspecteurDE = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDemandeInspecteurDE).toEqual(new DemandeInspecteurDE());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemandeInspecteurDE })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInspecteurDE = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeInspecteurDE).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
