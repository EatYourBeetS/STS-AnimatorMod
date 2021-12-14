package eatyourbeets.cards.animator.series.Konosuba;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.UUID;

public class Iris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Iris.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None, true)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public Iris()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0,0,1, 1);

        SetAffinity_Light(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        buffs = CombatStats.GetCombatData(cardID, null);
        if (buffs == null)
        {
            buffs = new HashMap<>();
            CombatStats.SetCombatData(cardID, buffs);
        }

        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetFilter(c -> c.type == CardType.ATTACK)
        .SetOptions(false, false, false)
        .AddCallback((cards) ->
        { //
            for (AbstractCard c : cards) {
                GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(c));
                GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(c));
                GameActions.Last.Callback(() -> {
                    int amount = Math.min(secondaryValue,c.costForTurn + 1);
                    GameActions.Bottom.IncreaseScaling(p.hand, BaseMod.MAX_HAND_SIZE, Affinity.Light, amount)
                            .SetFilter(ca ->  buffs.getOrDefault(c.uuid, 0) < magicNumber)
                            .AddCallback(cards2 ->
                            {
                                for (AbstractCard c2 : cards2)
                                {
                                    JUtils.IncrementMapElement(buffs, c2.uuid, amount);
                                }
                            });
                });
            }
        });
    }
}