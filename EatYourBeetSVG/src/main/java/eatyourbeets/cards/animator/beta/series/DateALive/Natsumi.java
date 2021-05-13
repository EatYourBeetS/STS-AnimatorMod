package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.curse.Curse_Depression;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.HashMap;

public class Natsumi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Natsumi.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.Random);

    static
    {
        DATA.AddPreview(new Curse_Depression(), true);
    }

    private static HashMap<Integer, ArrayList<AbstractCard>> cardPool;

    public Natsumi()
    {
        super(DATA);

        Initialize(2, 0, 2);
        SetUpgrade(0,0, 1);

        SetScaling(1, 0, 0);
        SetExhaust(true);

        SetSynergy(Synergies.DateALive);
        SetSpellcaster();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.FIRE)
        .SetOptions(true, false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
        .SetOptions(false, false, false)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                for (AbstractCard card : cards)
                {
                    if (GameUtilities.IsCurseOrStatus(card))
                    {
                        GameActions.Bottom.MakeCardInDrawPile(new Curse_Depression());
                    }

                    GameActions.Bottom.ReplaceCard(card.uuid, AbstractDungeon.getCard(CardRarity.UNCOMMON).makeCopy());
                }
            }
        });
    }
}