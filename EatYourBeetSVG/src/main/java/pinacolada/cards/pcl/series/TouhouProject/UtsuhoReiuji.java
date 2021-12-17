package pinacolada.cards.pcl.series.TouhouProject;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class UtsuhoReiuji extends PCLCard
{
    public static final PCLCardData DATA = Register(UtsuhoReiuji.class)
            .SetAttack(3, CardRarity.UNCOMMON, PCLAttackType.Elemental, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeriesFromClassPackage(true);

    public UtsuhoReiuji()
    {
        super(DATA);

        Initialize(18, 0, 2, 2);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Light(0,0,3);

        SetAffinityRequirement(PCLAffinity.Light, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SMALL_EXPLOSION).forEach(d -> d.SetVFXColor(Color.GOLDENROD));

        PCLActions.Bottom.SelectFromPile(name, magicNumber, upgraded ? new CardGroup[] {p.hand, p.discardPile, p.drawPile} : new CardGroup[] {p.hand})
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        PCLActions.Bottom.Exhaust(card)
                                .ShowEffect(true, true)
                                .AddCallback(() -> {
                                    PCLActions.Delayed.Callback(() -> {
                                        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
                                            for (AbstractPower po : mo.powers) {
                                                if (po instanceof PCLTriggerablePower) {
                                                    PCLActions.Bottom.Add(((PCLTriggerablePower) po).Trigger());
                                                }
                                                else if (po instanceof PoisonPower) {
                                                    PCLActions.Bottom.Add(new PoisonLoseHpAction(mo, player, po.amount, AttackEffects.POISON));
                                                }
                                                else if (po instanceof HealthBarRenderPower) {
                                                    PCLActions.Bottom.DealDamage(player, mo, po.amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
                                                }
                                            }
                                        }
                                    });
                                });
                    }

                    if (cards.size() > 0 && info.CanActivateSemiLimited && TrySpendAffinity(PCLAffinity.Light) && info.TryActivateSemiLimited()) {
                        PCLActions.Bottom.GainEnergyNextTurn(cards.size());
                    }
                });
    }
}

