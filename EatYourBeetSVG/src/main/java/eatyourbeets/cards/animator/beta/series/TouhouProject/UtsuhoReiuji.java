package eatyourbeets.cards.animator.beta.series.TouhouProject;

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
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UtsuhoReiuji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(UtsuhoReiuji.class)
            .SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage(true);

    public UtsuhoReiuji()
    {
        super(DATA);

        Initialize(18, 0, 2, 2);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 1, 0);
        SetAffinity_Dark(2, 0, 0);
        SetAffinity_Light(0,0,3);

        SetAffinityRequirement(Affinity.Light, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.SMALL_EXPLOSION).forEach(d -> d.SetVFXColor(Color.GOLDENROD));

        GameActions.Bottom.SelectFromPile(name, magicNumber, upgraded ? new CardGroup[] {p.hand, p.discardPile, p.drawPile} : new CardGroup[] {p.hand})
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        GameActions.Bottom.Exhaust(card)
                                .ShowEffect(true, true)
                                .AddCallback(() -> {
                                    GameActions.Delayed.Callback(() -> {
                                        for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                                            for (AbstractPower po : mo.powers) {
                                                if (po instanceof CommonTriggerablePower) {
                                                    GameActions.Bottom.Add(((CommonTriggerablePower) po).Trigger());
                                                }
                                                else if (po instanceof PoisonPower) {
                                                    GameActions.Bottom.Add(new PoisonLoseHpAction(mo, player, po.amount, AttackEffects.POISON));
                                                }
                                                else if (po instanceof HealthBarRenderPower) {
                                                    GameActions.Bottom.DealDamage(player, mo, po.amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
                                                }
                                            }
                                        }
                                    });
                                });
                    }

                    if (cards.size() > 0 && info.CanActivateSemiLimited && TrySpendAffinity(Affinity.Light) && info.TryActivateSemiLimited()) {
                        GameActions.Bottom.GainEnergyNextTurn(cards.size());
                    }
                });
    }
}

