package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.utilities.GameActionsHelper;

public class Time extends UnnamedCard implements OnBattleStartSubscriber
{
    public static final String ID = CreateFullID(Time.class.getSimpleName());

    public Time()
    {
        super(ID, 1, CardType.POWER, CardRarity.BASIC, CardTarget.SELF);

        Initialize(0,0, 6, 1);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, 1);
        GameActionsHelper.GainTemporaryHP(p, p, magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
            upgradeSecondaryValue(1);
        }
    }

    @Override
    public void OnBattleStart()
    {
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(this.makeStatEquivalentCopy()));
        AbstractDungeon.player.increaseMaxHp(secondaryValue, true);
    }
}