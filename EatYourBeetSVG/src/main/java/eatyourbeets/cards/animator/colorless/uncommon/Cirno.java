package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Cirno extends AnimatorCard
{
    public static final String ID = Register(Cirno.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Cirno()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(4, 0);

        SetEthereal(true);
        SetMultiDamage(true);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ChannelOrb(new Frost(), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //GameActions.Bottom.VFX(new BlizzardEffect(1, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.4f);
        GameActions.Bottom.Callback(__ ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + 5;

            for (int i = 0; i < frostCount; i++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });

        GameActions.Bottom.ChannelOrb(new Frost(), true);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_VERTICAL).SetOptions2(true, false);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}

