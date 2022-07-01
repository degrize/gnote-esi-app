import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDemandeInspecteurEtudiant, DemandeInspecteurEtudiant } from '../demande-inspecteur-etudiant.model';
import { DemandeInspecteurEtudiantService } from '../service/demande-inspecteur-etudiant.service';

import { DemandeInspecteurEtudiantRoutingResolveService } from './demande-inspecteur-etudiant-routing-resolve.service';

describe('DemandeInspecteurEtudiant routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DemandeInspecteurEtudiantRoutingResolveService;
  let service: DemandeInspecteurEtudiantService;
  let resultDemandeInspecteurEtudiant: IDemandeInspecteurEtudiant | undefined;

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
    routingResolveService = TestBed.inject(DemandeInspecteurEtudiantRoutingResolveService);
    service = TestBed.inject(DemandeInspecteurEtudiantService);
    resultDemandeInspecteurEtudiant = undefined;
  });

  describe('resolve', () => {
    it('should return IDemandeInspecteurEtudiant returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInspecteurEtudiant = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeInspecteurEtudiant).toEqual({ id: 123 });
    });

    it('should return new IDemandeInspecteurEtudiant if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInspecteurEtudiant = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDemandeInspecteurEtudiant).toEqual(new DemandeInspecteurEtudiant());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemandeInspecteurEtudiant })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeInspecteurEtudiant = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeInspecteurEtudiant).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
