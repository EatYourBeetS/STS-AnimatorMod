package eatyourbeets.cards.unnamed;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.ArrayList;

public class DarkBomb extends UnnamedCard
{
    public static final String ID = CreateFullID(DarkBomb.class.getSimpleName());

    public DarkBomb()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(20, 0, 2);

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActionsHelper.ChannelOrb(new Dark(), true);
        }

        ArrayList<AbstractCreature> characters = PlayerStatistics.GetAllCharacters(true);
        GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.VIOLET)));
        for (int i = 0; i < 3; i++)
        {
            for (AbstractCreature c : characters)
            {
                ExplosionEffect(c);
            }
        }

        for (AbstractCreature c : characters)
        {
            GameActionsHelper.DamageTarget(p, c, this, AbstractGameAction.AttackEffect.NONE, true);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.isInnate = true;
        }
    }

    private void ExplosionEffect(AbstractCreature target)
    {
        float x = target.hb.cX + AbstractDungeon.miscRng.random(-40, 40);
        float y = target.hb.cY + AbstractDungeon.miscRng.random(-40, 40);
        GameActionsHelper.AddToBottom(new WaitAction(0.1f));
        GameActionsHelper.AddToBottom(new VFXAction(new ExplosionSmallEffect(x, y), 0F));
    }
}