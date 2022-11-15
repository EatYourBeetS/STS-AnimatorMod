package eatyourbeets.cards.animator.colorless.rare;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.WeightedList;

public class GiftBox extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GiftBox.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(-1)
            .SetColor(CardColor.COLORLESS);

    public GiftBox()
    {
        super(DATA);

        Initialize(0, 0, 5, 12);

        SetAffinity_Star(1);
        SetPurge(true);

        SetObtainableInCombat(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        Random rng = CombatStats.GetCombatData(cardID + "rng", null);
        if (rng == null)
        {
            rng = new Random(Settings.seed + (AbstractDungeon.actNum * 37) + (AbstractDungeon.floorNum * 53));
            CombatStats.SetCombatData(cardID + "rng", rng);
        }

        if (info.TryActivateLimited())
        {
            GameActions.Bottom.GainGold(secondaryValue);
        }

        GameActions.Bottom.DiscardFromHand(name, BaseMod.MAX_HAND_SIZE, false)
        .SetOptions(false, false, true);

        final WeightedList<AbstractCard> cards = GameUtilities.GetCardsInCombatWeighted(null);
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.MakeCardInHand(cards.Retrieve(rng))
            .SetUpgrade(upgraded, true)
            .SetDuration(0.01f, true);
        }
    }
}