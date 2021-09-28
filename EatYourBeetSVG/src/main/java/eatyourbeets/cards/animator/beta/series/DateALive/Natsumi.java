package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.curse.Curse_Depression;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.HashMap;

public class Natsumi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Natsumi.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_Depression(), false));

    private static HashMap<Integer, ArrayList<AbstractCard>> cardPool;

    public Natsumi()
    {
        super(DATA);

        Initialize(2, 0, 2);
        SetUpgrade(0,0, 1);
        SetAffinity_Blue(2, 0, 1);
        SetExhaust(true);

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.FIRE)
        .SetOptions(true, false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                for (AbstractCard card : cards)
                {
                    if (GameUtilities.IsHindrance(card))
                    {
                        GameActions.Bottom.MakeCardInDrawPile(new Curse_Depression());
                    }

                    GameActions.Bottom.ReplaceCard(card.uuid, AbstractDungeon.getCard(CardRarity.UNCOMMON).makeCopy());
                }
            }
        });
    }
}