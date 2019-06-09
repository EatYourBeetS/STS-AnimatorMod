package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import eatyourbeets.cards.AnimatorCard_Status;
import eatyourbeets.powers.PlayerStatistics;

public class TheHaunt extends AnimatorCard_Status
{
    public static final String ID = CreateFullID(TheHaunt.class.getSimpleName());

    public TheHaunt()
    {
        super(ID, 1, CardRarity.RARE, CardTarget.NONE);

        Initialize(0,0, 10 + (PlayerStatistics.SaveData.TheHaunt * 5));

        this.isEthereal = true;
        this.exhaust = true;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        // Do not auto play
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for(int i = 0; i < this.magicNumber; ++i)
        {
            AbstractDungeon.effectList.add(new GainPennyEffect(p.hb.cX, p.hb.cY + (p.hb.height / 2)));
        }
        p.gainGold(magicNumber);
        PlayerStatistics.SaveData.TheHaunt += 1;
    }
}