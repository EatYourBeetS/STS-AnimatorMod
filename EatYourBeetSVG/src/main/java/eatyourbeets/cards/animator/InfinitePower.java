package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.blights.Doomed;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.interfaces.OnAddedToDeckSubscriber;
import eatyourbeets.utilities.GameActionsHelper;

public class InfinitePower extends AnimatorCard_UltraRare implements OnAddedToDeckSubscriber
{
    public static final String ID = CreateFullID(InfinitePower.class.getSimpleName());

    public InfinitePower()
    {
        super(ID, 0, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0);

        AddExtendedDescription();

        AutoplayField.autoplay.set(this, true);
        this.isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 10), 10);
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    public void OnAddedToDeck()
    {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.getCurrRoom().spawnBlightAndObtain(p.hb.cX, p.hb.cY, new Doomed());
    }
}