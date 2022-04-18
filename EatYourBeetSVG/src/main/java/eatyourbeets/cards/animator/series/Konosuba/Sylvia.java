package eatyourbeets.cards.animator.series.Konosuba;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Sylvia_Chimera;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Sylvia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sylvia.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.Konosuba_Sylvia(4, Hans.DATA, Verdia.DATA, Sylvia_Chimera.DATA));
                data.AddPreview(new Sylvia_Chimera(), true);
            });

    public Sylvia()
    {
        super(DATA);

        Initialize(0, 6, 0, 3);
        SetUpgrade(0, 2, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return magicNumber > 0 ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        int amount = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (uuid != c.uuid && GameUtilities.GetAffinityLevel(c, Affinity.Blue, false) > 0)
            {
                amount += secondaryValue;
            }
        }

        GameUtilities.ModifyMagicNumber(this, amount, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ModifyAffinityLevel(p.hand, BaseMod.MAX_HAND_SIZE, Affinity.Blue, -1, true)
        .Flash(Color.RED)
        .SetFilter(c -> c.uuid != uuid)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.GainTemporaryHP(cards.size() * secondaryValue);
            }

            if (affinities.GetLevel(Affinity.Star) <= 0)
            {
                for (AbstractCard c : cards)
                {
                    final RandomizedList<Affinity> list = new RandomizedList<>();
                    for (Affinity a : Affinity.Basic())
                    {
                        if (affinities.GetLevel(a) < 2)
                        {
                            list.Add(a);
                        }
                    }

                    if (list.Size() > 0)
                    {
                        affinities.Add(list.Retrieve(rng), 1);
                    }
                }
            }
        });

        if (info.IsSynergizing)
        {
            GameActions.Bottom.Cycle(name, 1);
        }
    }
}