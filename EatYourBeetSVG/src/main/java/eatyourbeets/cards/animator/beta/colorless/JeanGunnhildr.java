package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class JeanGunnhildr extends AnimatorCard {
    public static final EYBCardData DATA = Register(JeanGunnhildr.class).SetAttack(1, CardRarity.RARE);

    public JeanGunnhildr() {
        super(DATA);

        Initialize(8, 0, 1);
        SetUpgrade(3, 0, 0);
        SetScaling(0, 1, 1);

        SetLoyal(true);
        SetSynergy(Synergies.GenshinImpact);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (CombatStats.TryActivateSemiLimited(cardID)){
            GameActions.Bottom.DiscardFromHand(name, 1, false)
                    .SetOptions(true, true, true)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0) {
                            int cost = cards.get(0).cost;

                            if (cost >= 3) {
                                GameActions.Top.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
                            } else {
                                GameActions.Top.GainAgility(magicNumber, upgraded);
                            }
                        }
                    });
        }

    }
}