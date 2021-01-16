package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.rewards.animator.UpgradeCommonReward;

public class Rorona extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rorona.class).SetSkill(2, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS);

    public Rorona()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetEthereal(true);
        SetPurge(true);

        SetSynergy(Synergies.Atelier);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.getCurrRoom().rewards.add(new UpgradeCommonReward());
    }
}