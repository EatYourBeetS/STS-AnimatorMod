package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.OnAddedToDeckSubscriber;
import eatyourbeets.powers.animator.KaijinPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

public class Kaijin extends AnimatorCard implements OnAddedToDeckSubscriber
{
    public static final String ID = Register(Kaijin.class, EYBCardBadge.Special);

    public Kaijin()
    {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new KaijinPower(p, magicNumber));
    }

    @Override
    public void OnAddedToDeck()
    {
        GameEffects.Queue.Callback(new WaitAction(0.05f), __ ->
        {
            RandomizedList<AbstractCard> upgradableCards = new RandomizedList<>();
            for (AbstractCard c : player.masterDeck.group)
            {
                if (CardRarity.BASIC.equals(c.rarity) && c.canUpgrade())
                {
                    upgradableCards.Add(c);
                }
            }

            if (upgradableCards.Count() > 0)
            {
                AbstractCard card = upgradableCards.Retrieve(AbstractDungeon.cardRandomRng);
                card.upgrade();
                player.bottledCardUpgradeCheck(card);

                GameEffects.TopLevelList.ShowCardBriefly(card.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F);
                GameEffects.TopLevelList.Add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            }
        });
    }
}