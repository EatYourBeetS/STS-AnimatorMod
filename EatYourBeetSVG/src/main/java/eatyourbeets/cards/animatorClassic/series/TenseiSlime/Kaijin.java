package eatyourbeets.cards.animatorClassic.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.powers.animatorClassic.KaijinPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

public class Kaijin extends AnimatorClassicCard implements OnAddToDeckListener
{
    public static final EYBCardData DATA = Register(Kaijin.class).SetPower(1, CardRarity.UNCOMMON);

    public Kaijin()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetCostUpgrade(-1);

        SetSeries(CardSeries.TenseiSlime);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new KaijinPower(p, magicNumber));
    }

    @Override
    public boolean OnAddToDeck()
    {
        GameEffects.Queue.Callback(new WaitAction(0.05f), () ->
        {
            RandomizedList<AbstractCard> upgradableCards = new RandomizedList<>();
            for (AbstractCard c : player.masterDeck.group)
            {
                if (CardRarity.BASIC.equals(c.rarity) && c.canUpgrade())
                {
                    upgradableCards.Add(c);
                }
            }

            if (upgradableCards.Size() > 0)
            {
                AbstractCard card = upgradableCards.Retrieve(AbstractDungeon.miscRng);
                card.upgrade();
                player.bottledCardUpgradeCheck(card);

                final float x = Settings.WIDTH * 0.5f;
                final float y = Settings.HEIGHT * 0.5f;

                GameEffects.TopLevelList.ShowCardBriefly(card.makeStatEquivalentCopy(), x + AbstractCard.IMG_WIDTH * 0.5f + 20f * Settings.scale, y);
                GameEffects.TopLevelList.Add(new UpgradeShineEffect(x, y));
            }
        });

        return true;
    }
}